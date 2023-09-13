package com.modu.authorizationServer;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Slf4j
public class BcryptPasswordEncoderTest {

    // BcryptPasswordEncoder 가 서로 다른 객체여도 암복호화에 이상 없음.
    // 한 객체로 암호화한 다음에 다른 객체로 복호화해도 잘 됨.
    // 회원 DB에는 비밀번호가 Bcrypt 로 암호화된 값으로 저장되어있음.
    // 인가서버를 재기동하면서 BcryptPasswordEncoder 빈이 새로 생성되더라도
    // 회원 DB에 있는 비밀번호 값을 문제없이 복호화할 수 있음.
    @Test
    void BcryptPasswordEncoder_서로다른객체_비밀번호인증() {
        BCryptPasswordEncoder bCryptPasswordEncoder1 = new BCryptPasswordEncoder();
        BCryptPasswordEncoder bCryptPasswordEncoder2 = new BCryptPasswordEncoder();
        log.info("bCryptPasswordEncoder1={}", bCryptPasswordEncoder1);
        log.info("bCryptPasswordEncoder2={}", bCryptPasswordEncoder2);
        String password = "password";
        String encryptedPassword = bCryptPasswordEncoder1.encode(password);

        boolean result = bCryptPasswordEncoder2.matches(password, encryptedPassword);
        Assertions.assertThat(result).isTrue();
    }
}
