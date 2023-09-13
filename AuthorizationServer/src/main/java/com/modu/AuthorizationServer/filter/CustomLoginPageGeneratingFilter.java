package com.modu.authorizationServer.filter;

/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.HtmlUtils;

/**
 * For internal use with namespace configuration in the case where a user doesn't
 * configure a login page. The configuration code will insert this filter in the chain
 * instead.
 *
 * Will only work if a redirect is used to the login page.
 *
 * @author Luke Taylor
 * @since 2.0
 */
@Component
public class CustomLoginPageGeneratingFilter extends GenericFilterBean {

    public static final String DEFAULT_LOGIN_PAGE_URL = "/login";

    public static final String ERROR_PARAMETER_NAME = "error";

    private String loginPageUrl;

    private String logoutSuccessUrl;

    private String failureUrl;

    private String authenticationUrl = "/login";

    private String emailParameter = "email";

    private String passwordParameter = "password";

    private Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs = (request) -> Collections.emptyMap();

    public CustomLoginPageGeneratingFilter() {
        this.loginPageUrl = DEFAULT_LOGIN_PAGE_URL;
        this.logoutSuccessUrl = DEFAULT_LOGIN_PAGE_URL + "?logout";
        this.failureUrl = DEFAULT_LOGIN_PAGE_URL + "?" + ERROR_PARAMETER_NAME;
        setResolveHiddenInputs(this::hiddenInputs);
    }

    /**
     * Sets a Function used to resolve a Map of the hidden inputs where the key is the
     * name of the input and the value is the value of the input. Typically this is used
     * to resolve the CSRF token.
     * @param resolveHiddenInputs the function to resolve the inputs
     */
    public void setResolveHiddenInputs(Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs) {
        Assert.notNull(resolveHiddenInputs, "resolveHiddenInputs cannot be null");
        this.resolveHiddenInputs = resolveHiddenInputs;
    }

    private Map<String, String> hiddenInputs(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        return (token != null) ? Collections.singletonMap(token.getParameterName(), token.getToken())
                : Collections.emptyMap();
    }


    public void setLogoutSuccessUrl(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
    }

    public String getLoginPageUrl() {
        return this.loginPageUrl;
    }

    public void setLoginPageUrl(String loginPageUrl) {
        this.loginPageUrl = loginPageUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }


    public void setAuthenticationUrl(String authenticationUrl) {
        this.authenticationUrl = authenticationUrl;
    }

    public void setEmailParameter(String emailParameter) {
        this.emailParameter = emailParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        this.passwordParameter = passwordParameter;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        boolean loginError = isErrorPage(request);
        boolean logoutSuccess = isLogoutSuccess(request);
        if (isLoginUrlRequest(request) || loginError || logoutSuccess) {
            String loginPageHtml = generateLoginPageHtml(request, loginError, logoutSuccess);
            response.setContentType("text/html;charset=UTF-8");
            response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
            response.getWriter().write(loginPageHtml);
            return;
        }
        chain.doFilter(request, response);
    }

    private String generateLoginPageHtml(HttpServletRequest request, boolean loginError, boolean logoutSuccess) {
        String errorMsg = loginError ? getLoginErrorMessage(request) : "Invalid credentials";
        String contextPath = request.getContextPath();
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"ko\">\n");
        sb.append("  <head>\n");
        sb.append("    <meta charset=\"utf-8\">\n");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n");
        sb.append("    <meta name=\"description\" content=\"\">\n");
        sb.append("    <meta name=\"author\" content=\"\">\n");
        sb.append("    <title>로그인</title>\n");
        sb.append("    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" "
                + "rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n");
        sb.append("    <link href=\"https://getbootstrap.com/docs/4.0/examples/signin/signin.css\" "
                + "rel=\"stylesheet\" crossorigin=\"anonymous\"/>\n");
        sb.append("  </head>\n");
        sb.append("  <body>\n");
        sb.append("     <div class=\"container\">\n");

        sb.append("      <form class=\"form-signin\" method=\"post\" action=\"" + contextPath
                + this.authenticationUrl + "\">\n");
        sb.append("        <h2 class=\"form-signin-heading\">Please sign in</h2>\n");
        sb.append(createError(loginError, errorMsg) + createLogoutSuccess(logoutSuccess) + "        <p>\n");
        sb.append("          <label for=\"email\" class=\"sr-only\">Email</label>\n");
        sb.append("          <input type=\"text\" id=\"email\" name=\"" + this.emailParameter
                + "\" class=\"form-control\" placeholder=\"이메일\" required autofocus>\n");
        sb.append("        </p>\n");
        sb.append("        <p>\n");
        sb.append("          <label for=\"password\" class=\"sr-only\">Password</label>\n");
        sb.append("          <input type=\"password\" id=\"password\" name=\"" + this.passwordParameter
                + "\" class=\"form-control\" placeholder=\"비밀번호\" required>\n");
        sb.append("        </p>\n");
        sb.append(renderHiddenInputs(request));
        sb.append("        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">로그인</button>\n");
        sb.append("      </form>\n");

        sb.append("</div>\n");
        sb.append("</body></html>");
        return sb.toString();
    }

    private String getLoginErrorMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null &&
                session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) instanceof AuthenticationException exception) {
            return exception.getMessage();
        }
        return "Invalid credentials";
    }

    private String renderHiddenInputs(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> input : this.resolveHiddenInputs.apply(request).entrySet()) {
            sb.append("<input name=\"");
            sb.append(input.getKey());
            sb.append("\" type=\"hidden\" value=\"");
            sb.append(input.getValue());
            sb.append("\" />\n");
        }
        return sb.toString();
    }

    private boolean isLogoutSuccess(HttpServletRequest request) {
        return this.logoutSuccessUrl != null && matches(request, this.logoutSuccessUrl);
    }

    private boolean isLoginUrlRequest(HttpServletRequest request) {
        return matches(request, this.loginPageUrl);
    }

    private boolean isErrorPage(HttpServletRequest request) {
        return matches(request, this.failureUrl);
    }

    private String createError(boolean isError, String message) {
        if (!isError) {
            return "";
        }
        return "<div class=\"alert alert-danger\" role=\"alert\">" + HtmlUtils.htmlEscape(message) + "</div>";
    }

    private String createLogoutSuccess(boolean isLogoutSuccess) {
        if (!isLogoutSuccess) {
            return "";
        }
        return "<div class=\"alert alert-success\" role=\"alert\">You have been signed out</div>";
    }

    private boolean matches(HttpServletRequest request, String url) {
        if (!"GET".equals(request.getMethod()) || url == null) {
            return false;
        }
        String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');
        if (pathParamIndex > 0) {
            // strip everything after the first semi-colon
            uri = uri.substring(0, pathParamIndex);
        }
        if (request.getQueryString() != null) {
            uri += "?" + request.getQueryString();
        }
        if ("".equals(request.getContextPath())) {
            return uri.equals(url);
        }
        return uri.equals(request.getContextPath() + url);
    }

}

