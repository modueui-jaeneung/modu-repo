package com.modu.MemberServer;

import com.modu.MemberServer.entity.Follow;
import com.modu.MemberServer.entity.Member;
import com.modu.MemberServer.entity.enums.SocialType;
import com.modu.MemberServer.service.FollowService;
import com.modu.MemberServer.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private FollowService followService;

    @Test
    void 회원가입() {

    }

    @Test
    void 회원단건조회() {

    }

    @Test
    void 회원찾고_팔로우까지조회하기() {



    }
}
