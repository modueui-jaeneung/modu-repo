package com.modu.authorizationServer.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.Map;

@Controller
@Slf4j
public class LoginController {

    @GetMapping("/loginmvc")
    public String loginPage(HttpServletRequest request, Model model) {
        log.info("LoginController 진입");
        Map<String, String> hiddenMap = hiddenInputs(request);
        if (hiddenMap.containsKey("_csrf")) {
            model.addAttribute("_csrf", hiddenMap.get("_csrf"));
            log.info("_csrf={}", hiddenMap.get("_csrf"));
        }

        // 로그인페이지에 타임리프 도입해서 Map 객체 꺼내어 input hidden 태그 추가하기
        return "login";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    private Map<String, String> hiddenInputs(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        log.info("token.getParameterName()={}", token.getParameterName());
        return (token != null) ? Collections.singletonMap(token.getParameterName(), token.getToken())
                : Collections.emptyMap();
    }
}
