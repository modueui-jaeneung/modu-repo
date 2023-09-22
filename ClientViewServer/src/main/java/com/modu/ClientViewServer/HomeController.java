package com.modu.ClientViewServer;

import com.modu.ClientViewServer.config.EnvironmentValueConfig;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final RestTemplate restTemplate;
    private final EnvironmentValueConfig environmentValueConfig;

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

    @GetMapping("/signup")
    public String signup(Model model) {

        String kakaoRestApiKey = environmentValueConfig.kakaoRestApiKey;
        model.addAttribute("kakaoRestApiKey", kakaoRestApiKey);

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
        map.put("introduceMyself", List.of(signUpDto.getIntroduceMyself()));
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

        ResponseEntity<Message> response;
        try {
            response = restTemplate.exchange(uriString, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});
            if (response.getStatusCode() == HttpStatus.CREATED) {
                return "index";
            }
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return "signup";
        }

        return "signup";
    }

    /**
     * 회원정보 수정 페이지 보여주기
     * 인증된 사용자만 접근할 수 있도록 해야 함.
     * 게이트웨이 서버에 access_token 을 헤더에 담아서 GET /members/info 요청함
     */
    @GetMapping("/updateInfo")
    public String updateInfo(HttpServletRequest request, Model model) {

        Cookie[] cookies = request.getCookies();
        String access_token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    access_token = cookie.getValue();
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        if (access_token != null) {
            headers.put("Authorization", List.of("Bearer " + access_token));
        }
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String uriString = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(9001)
                .path("/members/info")
                .build().toUriString();

        try {
            ResponseEntity<MemberInfoResponseDto> memberInfoResponse = restTemplate.exchange(uriString, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
            });
            if (memberInfoResponse.getStatusCode() != HttpStatus.OK) {
                return "index";
            }

            log.info("memberInfo={}", memberInfoResponse.getBody());
            model.addAttribute("memberInfo", memberInfoResponse.getBody());

            String kakaoRestApiKey = environmentValueConfig.kakaoRestApiKey;
            model.addAttribute("kakaoRestApiKey", kakaoRestApiKey);

            return "updateInfo";
        } catch (HttpClientErrorException e) {
            return "index";
        }
    }

    @PostMapping("/updateInfo")
    public String updateInfo(@ModelAttribute UpdateMemberDto updateMemberDto, HttpServletRequest request) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("email", List.of(updateMemberDto.getEmail()));
        map.put("password", List.of(updateMemberDto.getPassword()));
        map.put("repeatPassword", List.of(updateMemberDto.getRepeatPassword()));
        map.put("nickname", List.of(updateMemberDto.getNickname()));
        map.put("address", List.of(updateMemberDto.getAddress()));
        map.put("introduceMyself", List.of(updateMemberDto.getIntroduceMyself()));
        String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(9001)
                .path("/members/info")
                .queryParams(map)
                .build().toUriString();

        HttpHeaders headers = new HttpHeaders();

        Cookie[] cookies = request.getCookies();
        String access_token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    access_token = cookie.getValue();
                }
            }
        }
        if (access_token != null) {
            headers.put("Authorization", List.of("Bearer " + access_token));
        }
        HttpEntity<SignUpDto> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Message> response = restTemplate.exchange(uriString, HttpMethod.PUT, entity, new ParameterizedTypeReference<>() {
            });
            if (response.getStatusCode() == HttpStatus.OK) {
                return "index";
            }
        } catch (HttpClientErrorException e) {
            return "redirect:/updateInfo";
        }
        return "index";
    }

    @GetMapping("/bookmark")
    public String bookmark() {
        return "bookmark";
    }

    @GetMapping("/applyList")
    public String applyList() {
        return "applyList";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        String message;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberInfoResponseDto {
        String email;
        String nickname;
        String address;
        String introduceMyself;
    }
}
