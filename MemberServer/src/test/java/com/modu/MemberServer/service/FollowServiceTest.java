package com.modu.MemberServer.service;

import com.modu.MemberServer.dto.SignUpDto;
import com.modu.MemberServer.entity.Follow;
import com.modu.MemberServer.entity.Member;
import com.modu.MemberServer.exception.NoFollowException;
import com.modu.MemberServer.service.FollowService;
import com.modu.MemberServer.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class FollowServiceTest {

    @Autowired
    private FollowService followService;
    @Autowired
    private MemberService memberService;

    @Test
    void 팔로우생성() {
        SignUpDto signUpDto = createSignUpDto();
        Long memberId1 = memberService.signupLocal(signUpDto);
        Member findMember1 = memberService.findById(memberId1);

        SignUpDto signUpDto2 = createSignUpDto();
        signUpDto2.setEmail("user@bbb.com");
        signUpDto2.setNickname("bbb");
        Long memberId2 = memberService.signupLocal(signUpDto2);
        Member findMember2 = memberService.findById(memberId2);

        Long savedFollowId = followService.save(findMember1.getId(), findMember2.getId());
        Follow follow = followService.findById(savedFollowId);

        assertThat(follow).isNotNull();
        assertThat(follow.getFromMember()).isNotNull();
        assertThat(follow.getToMember()).isNotNull();
    }

    @Test
    void 팔로우취소() {
        SignUpDto signUpDto = createSignUpDto();
        Long memberId1 = memberService.signupLocal(signUpDto);
        Member findMember1 = memberService.findById(memberId1);

        SignUpDto signUpDto2 = createSignUpDto();
        signUpDto2.setEmail("user@bbb.com");
        signUpDto2.setNickname("bbb");
        Long memberId2 = memberService.signupLocal(signUpDto2);
        Member findMember2 = memberService.findById(memberId2);

        Long savedFollowId = followService.save(findMember1.getId(), findMember2.getId());

        followService.deleteFollow(findMember1.getId(), findMember2.getId());

        assertThrows(NoSuchElementException.class, () -> followService.findById(savedFollowId));

    }
    private SignUpDto createSignUpDto() {
        return new SignUpDto(
                "user@aaa.com",
                "pass",
                "pass",
                "user",
                "address~"
        );
    }
}
