package com.modu.authorizationServer.service;

import com.modu.authorizationServer.converter.ProviderUserRequest;
import com.modu.authorizationServer.exception.SocialSignupException;
import com.modu.authorizationServer.model.PrincipalUser;
import com.modu.authorizationServer.model.ProviderUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends AbstractOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // google, naver, kakao 중 어느 것인지 판단하기
        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oAuth2User);
        ProviderUser providerUser = super.providerUser(providerUserRequest);

        // 회원가입 or 이미 있으면 pass
        ResponseEntity<Object> responseEntity;
        Long memberId;
        LinkedHashMap bodyMap;
        try {
            responseEntity = super.findByEmailAndProvider(providerUser.getEmail(), providerUser.getProvider());
            bodyMap = (LinkedHashMap) responseEntity.getBody();
            memberId = Long.parseLong(bodyMap.get("id").toString());
        } catch (Exception e) {
            // 아직 회원이 아님
            // 회원가입 먼저 해야 함.
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            queryParams.add("email", providerUser.getEmail());
            queryParams.add("socialType", providerUser.getSocialType());
            queryParams.add("nickname", providerUser.getNickname());
            String uriString = UriComponentsBuilder
                    .newInstance()
                    .scheme("http")
                    .host("127.0.0.1")
                    .port(8083)
                    .path("/members/social")
                    .queryParams(queryParams)
                    .build().toUriString();

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Object> signupResponseEntity;
            signupResponseEntity = restTemplate.exchange(uriString, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});

            bodyMap = (LinkedHashMap) signupResponseEntity.getBody();
            memberId = Long.parseLong(bodyMap.get("message").toString());
        }

        // 로그인 진행
        return new PrincipalUser(providerUser, memberId);
    }
}
