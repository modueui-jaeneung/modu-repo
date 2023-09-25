package com.modu.authorizationServer.converter;

import com.modu.authorizationServer.model.NaverUser;
import com.modu.authorizationServer.model.ProviderUser;
import com.modu.authorizationServer.utils.OAuth2Utils;

public class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            return null;
        }
        return new NaverUser(OAuth2Utils.getSubAttributes(providerUserRequest.getOAuth2User(), "response"),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration());
    }
}
