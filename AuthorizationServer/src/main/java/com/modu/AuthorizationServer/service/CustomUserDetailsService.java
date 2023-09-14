package com.modu.authorizationServer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("userDetailsService")
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final String DEFAULT_SOCIAL_TYPE = "LOCAL";

    private final RestTemplate restTemplate;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("email", email);
        queryParams.add("socialType", DEFAULT_SOCIAL_TYPE);
        String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(8083)
                .path("/members/id")
                .queryParams(queryParams)
                .build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(uriString, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});

        // ResponseEntity의 제네릭 타입을 Object로 할 경우(기본값) response.getBody() 의 타입은 LinkedHashMap
        // key-value 로 꺼내 쓸 수 있는 구조

        User user = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            // 회원 id, 암호화된 password 를 잘 가져옴
            LinkedHashMap bodyMap = (LinkedHashMap) response.getBody();
            Integer id = (Integer) bodyMap.get("id");
            String encryptedPassword = (String) bodyMap.get("encryptedPassword");
            user = new User(id.toString(), encryptedPassword, List.of(new SimpleGrantedAuthority("USER")));
        } else {
            // 인증 x
            throw new IllegalStateException("로그인 실패...");
        }

        return user;
    }
}
