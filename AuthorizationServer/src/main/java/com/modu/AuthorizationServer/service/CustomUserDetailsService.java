package com.modu.authorizationServer.service;

import com.modu.authorizationServer.model.SocialProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        ResponseEntity<Object> responseEntity = super.findByEmailAndProvider(email, SocialProvider.LOCAL.toString());

        // ResponseEntity의 제네릭 타입을 Object로 할 경우(기본값) response.getBody() 의 타입은 LinkedHashMap
        // key-value 로 꺼내 쓸 수 있는 구조

        User user;
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // 회원 id, 암호화된 password 를 잘 가져옴
            LinkedHashMap bodyMap = (LinkedHashMap) responseEntity.getBody();
            Long id = (Long) bodyMap.get("id");
            String encryptedPassword = (String) bodyMap.get("encryptedPassword");
            user = new User(id.toString(), encryptedPassword, List.of(new SimpleGrantedAuthority("USER")));
        } else {
            // 인증 x
            throw new IllegalStateException("로그인 실패...");
        }

        return user;
    }
}
