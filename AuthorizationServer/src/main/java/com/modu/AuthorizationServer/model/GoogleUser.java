package com.modu.authorizationServer.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleUser extends OAuth2ProviderUser {

    public GoogleUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getMainAttributes(), oAuth2User, clientRegistration);
    }


    @Override
    public String getSocialType() {
        return "GOOGLE";
    }

    @Override
    public String getNickname() {
        return (String) getAttributes().get("name");
    }

    @Override
    public String getEmail() {
        return (String) getAttributes().get("email");
    }

    @Override
    public String getProvider() {
        return "GOOGLE";
    }

    @Override
    public SocialProvider getSocialProvider() {
        return SocialProvider.GOOGLE;
    }


}
