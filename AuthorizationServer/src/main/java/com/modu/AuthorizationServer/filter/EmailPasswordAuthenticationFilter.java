package com.modu.authorizationServer.filter;

import com.modu.authorizationServer.provider.FormAuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class EmailPasswordAuthenticationFilter extends OncePerRequestFilter {

    private final String DEFAULT_API_LOGIN_URI = "/login";
    private final String emailParameter = "email";
    private final String passwordParameter = "password";

    private final FormAuthenticationProvider provider;
    private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();
    private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    private SecurityContextRepository securityContextRepository = null;
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public EmailPasswordAuthenticationFilter(FormAuthenticationProvider provider, SessionAuthenticationStrategy sessionStrategy, SecurityContextRepository securityContextRepository) {
        this.provider = provider;
        setSessionAuthenticationStrategy(sessionStrategy);
        setSecurityContextRepository(securityContextRepository);
    }

    protected String obtainEmail(HttpServletRequest request) {
        return request.getParameter(this.emailParameter);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().equals(DEFAULT_API_LOGIN_URI) || !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = obtainEmail(request);
        email = (email != null) ? email.trim() : "";
        String password = obtainPassword(request);
        password = (password != null) ? password : "";

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, password, null);
        auth.setAuthenticated(false);
        Authentication authentication = provider.authenticate(auth);

        if (!authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 세션 관련 작업들 (세션id, csrf)
        this.sessionStrategy.onAuthentication(authentication, request, response);

        // SecurityContextHolder 에 인증객체 저장
        successfulAuthentication(request, response, authentication);
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authentication));
        }
        // redirect uri 얻어내기 (code를 요청하기 위해)
        this.successHandler.onAuthenticationSuccess(request, response, authentication);
    }

    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }

    public void setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }
}
