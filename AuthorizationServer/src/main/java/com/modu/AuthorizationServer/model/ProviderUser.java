package com.modu.authorizationServer.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

public interface ProviderUser {

    String getSocialType();
    String getNickname();
    String getPassword();
    String getEmail();
    String getProvider();
    SocialProvider getSocialProvider();
    List<? extends GrantedAuthority> getAuthorities();
    Map<String, Object> getAttributes();

    OAuth2User getOAuth2User();
}
