package com.modu.ClientViewServer;

import com.modu.ClientViewServer.chat.ChatMessageDto;
import com.modu.ClientViewServer.chat.ChatRoomDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final RestTemplate restTemplate;

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
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

}
