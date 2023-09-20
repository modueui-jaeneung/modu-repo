package com.modu.OAuth2Client.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;


@Slf4j
public class RequestPostLogoutFilter extends OncePerRequestFilter {

    private final CsrfTokenRepository csrfTokenRepository;
    private final RestTemplate restTemplate;
    private String csrfRequestAttributeName = "_csrf";

    public RequestPostLogoutFilter(CsrfTokenRepository csrfTokenRepository, RestTemplate restTemplate) {
        this.csrfTokenRepository = csrfTokenRepository;
        this.restTemplate = restTemplate;
    }

    private RequestMatcher matcher = new AntPathRequestMatcher("/logout", "GET");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.matcher.matches(request)) {
            requestPostLogout(request, response);
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace(LogMessage.format("Did not request post logout uri since request did not match [%s]",
                        this.matcher));
            }
            filterChain.doFilter(request, response);
        }
    }

    private void requestPostLogout(HttpServletRequest request, HttpServletResponse response) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String uriString = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8081)
                .path("/logout")
                .queryParams(params)
                .build().toUriString();

        ResponseEntity<Object> responseFromPost = restTemplate.exchange(
                uriString,
                HttpMethod.POST,
                entity,
                Object.class
        );

        log.info("response from post and end");
    }
}
