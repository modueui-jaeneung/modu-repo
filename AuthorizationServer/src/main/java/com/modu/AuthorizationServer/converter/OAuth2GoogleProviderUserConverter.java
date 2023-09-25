package com.modu.authorizationServer.converter;

import com.modu.authorizationServer.model.GoogleUser;
import com.modu.authorizationServer.model.NaverUser;
import com.modu.authorizationServer.model.ProviderUser;
import com.modu.authorizationServer.utils.OAuth2Utils;

public class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.getClientRegistration().getRegistrationId().equals("google")) {
            return null;
        }
        return new GoogleUser(OAuth2Utils.getMainAttributes(providerUserRequest.getOAuth2User()),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration());
    }
}
