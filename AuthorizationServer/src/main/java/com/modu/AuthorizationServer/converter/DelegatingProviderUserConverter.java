package com.modu.authorizationServer.converter;

import com.modu.authorizationServer.model.ProviderUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class DelegatingProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    private List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> converters;

    public DelegatingProviderUserConverter() { init(); }

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        Assert.notNull(providerUserRequest, "providerUserRequest cannot be null");
        for (ProviderUserConverter<ProviderUserRequest, ProviderUser> converter : converters) {
            ProviderUser providerUser = converter.converter(providerUserRequest);
            if (providerUser != null) {
                return providerUser;
            }
        }
        return null;
    }

    public void init() {
        log.info("DelegatingProviderUserConverter.init()");
        List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> providerUserConverters =
                List.of(
                        new OAuth2NaverProviderUserConverter(),
                        new OAuth2GoogleProviderUserConverter(),
                        new OAuth2KakaoProviderUserConverter(),
                        new OAuth2KakaoOidcProviderUserConverter()
                );
        this.converters = Collections.unmodifiableList(new LinkedList<>(providerUserConverters));
    }
}
