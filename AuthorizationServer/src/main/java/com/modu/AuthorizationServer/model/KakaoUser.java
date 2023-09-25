package com.modu.authorizationServer.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoUser extends OAuth2ProviderUser {

    private Map<String, Object>
            otherAttributes;

    public KakaoUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {

        super(attributes.getSubAttributes(), oAuth2User, clientRegistration);
        this.otherAttributes = attributes.getOtherAttributes();
    }


    @Override
    public String getSocialType() {
        return "KAKAO";
    }

    @Override
    public String getNickname() {
        return (String) otherAttributes.get("nickname");
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
