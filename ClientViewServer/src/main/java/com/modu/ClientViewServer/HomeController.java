package com.modu.ClientViewServer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final RestTemplate restTemplate;

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {

        for (String key : request.getParameterMap().keySet()) {
            log.info("key={}, values={}", key, request.getParameter(key));
        }

        String tokenValue = request.getParameter("tokenValue");
        if (tokenValue != null) {
            log.info("tokenValue={}", tokenValue);
            Cookie cookie = new Cookie("access_token", tokenValue);
            cookie.setMaxAge(3600);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return "redirect:/";
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    Cookie responseCookie = new Cookie("access_token", cookie.getValue());
                    responseCookie.setMaxAge(3600);
                    responseCookie.setHttpOnly(true);
                    response.addCookie(responseCookie);
                }
            }
        }

        return "index";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignUpDto signUpDto) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("email", List.of(signUpDto.getEmail()));
        map.put("password", List.of(signUpDto.getPassword()));
        map.put("repeatPassword", List.of(signUpDto.getRepeatPassword()));
        map.put("nickname", List.of(signUpDto.getNickname()));
        map.put("address", List.of(signUpDto.getAddress()));
        String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(8083)
                .path("/members")
                .queryParams(map)
                .build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<SignUpDto> entity = new HttpEntity<>(headers);
        ResponseEntity<Message> response = restTemplate.exchange(uriString, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
        });

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return "index";
        }
        return "signup";

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        String message;
    }
}
