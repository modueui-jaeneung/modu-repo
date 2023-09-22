package com.modu.ClientViewServer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentValueConfig {

    @Value("${kakao.restApiKey}")
    public String kakaoRestApiKey;
}
