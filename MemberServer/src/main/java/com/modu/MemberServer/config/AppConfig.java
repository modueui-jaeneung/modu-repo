package com.modu.MemberServer.config;

import com.modu.MemberServer.utils.BCryptImpl;
import com.modu.MemberServer.utils.EncryptHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EncryptHelper encryptHelper() {
        return new BCryptImpl();
    }
}
