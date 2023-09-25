package com.modu.authorizationServer.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class NaverUser extends OAuth2ProviderUser {

    public NaverUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getSubAttributes(), oAuth2User, clientRegistration);
    }


    @Override
    public String getSocialType() {
        return "NAVER";
    }

    @Override
    public String getNickname() {
        return (String) getAttributes().get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) getAttributes().get("email");
    }

    @Override
    public String getProvider() {
        return "NAVER";
    }

    @Override
    public SocialProvider getSocialProvider() {
        return SocialProvider.NAVER;
    }


}
