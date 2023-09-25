package com.modu.authorizationServer.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KakaoOidcUser extends OAuth2ProviderUser {



    public KakaoOidcUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getMainAttributes(), oAuth2User, clientRegistration);
    }


    @Override
    public String getSocialType() {
        return "KAKAO";
    }

    @Override
    public String getNickname() {
        return (String) getAttributes().get("nickname");
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    @Override
    public SocialProvider getSocialProvider() {
        return SocialProvider.KAKAO;
    }
}
