package com.modu.authorizationServer.converter;

import lombok.Data;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
public class ProviderUserRequest {

    private ClientRegistration clientRegistration;
    private OAuth2User oAuth2User;

    public ProviderUserRequest(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        this.clientRegistration = clientRegistration;
        this.oAuth2User = oAuth2User;
    }
}
