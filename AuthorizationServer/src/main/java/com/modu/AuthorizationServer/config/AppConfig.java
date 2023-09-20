package com.modu.authorizationServer.config;

import com.modu.authorizationServer.utils.Pem;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;
import java.util.UUID;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws IOException {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);


//        log.info("private key");
//        saveFile("private.pem", privateKey.getEncoded());
//        log.info("public key");
//        saveFile("public.pem", publicKey.getEncoded());

//        writePemFile(privateKey, "RSA PRIVATE KEY", "private.pem");
//        writePemFile(publicKey, "RSA PUBLIC KEY", "public.pem");

        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void saveFile(String filename, byte[] data) throws IOException {
        FileOutputStream fos = null;
        try {
            int bytesRead;
            File filePath = new File("/Users/joseong-gyu/Documents/modu_keys", filename);
             fos = new FileOutputStream(filePath);
            fos.write(data);
            fos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            Objects.requireNonNull(fos).close();
        }
    }

    private void writePemFile(Key key, String description, String filename) throws IOException {
        Pem pemFile = new Pem(key, description);
        pemFile.write(filename);

        log.info("{}를 {} 파일로 내보냈습니다.", description, filename);
    }
}
