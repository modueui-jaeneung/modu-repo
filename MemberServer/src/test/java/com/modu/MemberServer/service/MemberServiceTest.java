package com.modu.MemberServer.service;

import com.modu.MemberServer.dto.SignUpDto;
import com.modu.MemberServer.dto.UpdateMemberDto;
import com.modu.MemberServer.entity.Member;
import com.modu.MemberServer.exception.DuplicateMemberException;
import com.modu.MemberServer.exception.EmailFormatNotSatisfiedException;
import com.modu.MemberServer.exception.PasswordNotEqualException;
import com.modu.MemberServer.exception.PasswordNotSatisfiedException;
import com.modu.MemberServer.repository.FollowRepository;
import com.modu.MemberServer.repository.MemberRepository;
import com.modu.MemberServer.service.FollowService;
import com.modu.MemberServer.service.MemberService;
import com.modu.MemberServer.utils.EncryptHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
public class MemberServiceTest {


    @Autowired
    private MemberService memberService;

    @Autowired
    private FollowService followService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private EncryptHelper encryptHelper;

    @BeforeEach
    void init() {
        followRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    void 회원가입_LOCAL() {
        SignUpDto signUpDto = createSignUpDto();
        Long memberId = memberService.signupLocal(signUpDto);
        Member findMember = memberService.findById(memberId);

        assertThat(findMember.getEmail()).isEqualTo(signUpDto.getEmail());
        assertThat(encryptHelper.isMatch(signUpDto.getPassword(), findMember.getPassword())).isTrue();
    }

    @Test
    void 회원가입_LOCAL_가입실패_비밀번호불일치() {
        SignUpDto signUpDto = new SignUpDto(
                "user@aaa.com",
                "passsssssssss",
                "pass",
                "user",
                "address~",
                "I love my body~"
        );
        assertThrows(PasswordNotEqualException.class, () -> memberService.signupLocal(signUpDto));
    }

    @Test
    void 회원가입_LOCAL_가입실패_이메일중복() {
        SignUpDto signUpDto1 = createSignUpDto();
        memberService.signupLocal(signUpDto1);
        SignUpDto signUpDto2 = new SignUpDto(
                "user@aaa.com",
                "pass123!",
                "pass123!",
                "userrrrrr",
                "addressssssss~",
                "I love my body~"
        );
        assertThrows(DuplicateMemberException.class, () -> memberService.signupLocal(signUpDto2));
    }

    @Test
    void 회원가입_LOCAL_가입실패_이메일형식안맞음() {
        SignUpDto signUpDto = createSignUpDto();
        signUpDto.setEmail("user.aaa.com");
        assertThrows(EmailFormatNotSatisfiedException.class, () -> memberService.signupLocal(signUpDto));
    }

    @Test
    void 회원가입_LOCAL_가입실패_비밀번호형식안맞음() {
        SignUpDto signUpDto = createSignUpDto();
        String password = "hellohellohello";
        signUpDto.setPassword(password);
        signUpDto.setRepeatPassword(password);
        assertThrows(PasswordNotSatisfiedException.class, () -> memberService.signupLocal(signUpDto));
    }

    @Test
    void 내가팔로우한회원_나한테팔로우한회원() {
        SignUpDto signUpDto = createSignUpDto();
        Long memberId = memberService.signupLocal(signUpDto);
        Member findMember1 = memberService.findById(memberId);

        SignUpDto signUpDto2 = createSignUpDto();
        signUpDto2.setEmail("user@bbb.com");
        signUpDto2.setNickname("bbb");
        Long memberId2 = memberService.signupLocal(signUpDto2);
        Member findMember2 = memberService.findById(memberId2);

        SignUpDto signUpDto3 = createSignUpDto();
        signUpDto3.setEmail("user@ccc.com");
        signUpDto3.setNickname("ccc");
        Long memberId3 = memberService.signupLocal(signUpDto3);
        Member findMember3 = memberService.findById(memberId3);

        followService.save(findMember1.getId(), findMember2.getId());
        followService.save(findMember1.getId(), findMember3.getId());

        // 1번이 팔로우 한 사람 2명
        List<Member> followingMembers = memberService.findFollowingMembers(memberId);
        assertThat(followingMembers.size()).isEqualTo(2);

        // 1번한테 팔로우 한 사람 0명
        List<Member> membersFollowingToMe1 = memberService.findMembersFollowingToMe(memberId);
        assertThat(membersFollowingToMe1.size()).isEqualTo(0);

        // 2번한테 팔로우 한 사람 1명 (1번이 팔로우함)
        List<Member> membersFollowingToMe2 = memberService.findMembersFollowingToMe(memberId2);
        assertThat(membersFollowingToMe2.size()).isEqualTo(1);
        assertThat(membersFollowingToMe2.get(0).getId()).isEqualTo(memberId);

        // 3번한테 팔로우 한 사람 1명 (1번이 팔로우함)
        List<Member> membersFollowingToMe3 = memberService.findMembersFollowingToMe(memberId3);
        assertThat(membersFollowingToMe3.size()).isEqualTo(1);
        assertThat(membersFollowingToMe3.get(0).getId()).isEqualTo(memberId);
    }

    @Test
    void 회원수정() {
        SignUpDto signUpDto = createSignUpDto();
        Long memberId = memberService.signupLocal(signUpDto);
        Member findMember = memberService.findById(memberId);

        String email = findMember.getEmail();
        String updateNickname = findMember.getNickname() + "~~~~~";
        String updatePassword = findMember.getPassword() + "~~~~~";
        String updateRepeatPassword = findMember.getPassword() + "~~~~~";
        String updateAddress = findMember.getAddress();
        String updateIntroduceMyself = findMember.getIntroduceMyself();
        UpdateMemberDto updateMemberDto = new UpdateMemberDto(
                email,
                updateNickname,
                updateAddress,
                updatePassword,
                updateRepeatPassword,
                updateIntroduceMyself);
        memberService.updateMember(findMember.getId(), updateMemberDto);

        Member updatedMember = memberService.findById(memberId);
        assertThat(updatedMember.getNickname()).isEqualTo(updateNickname);
        assertThat(updatedMember.getAddress()).isEqualTo(updateAddress);
        assertThat(encryptHelper.isMatch(updatePassword, updatedMember.getPassword())).isTrue();
        assertThat(updatedMember.getIntroduceMyself()).isEqualTo(updateIntroduceMyself);
        assertThat(updatedMember.getUpdatedAt().isEqual(findMember.getUpdatedAt())).isFalse();
    }

    @Test
    void 회원삭제() {
        SignUpDto signUpDto = createSignUpDto();
        Long memberId = memberService.signupLocal(signUpDto);
        Member findMember1 = memberService.findById(memberId);

        SignUpDto signUpDto2 = createSignUpDto();
        signUpDto2.setEmail("user@bbb.com");
        signUpDto2.setNickname("bbb");
        Long memberId2 = memberService.signupLocal(signUpDto2);
        Member findMember2 = memberService.findById(memberId2);

        SignUpDto signUpDto3 = createSignUpDto();
        signUpDto3.setEmail("user@ccc.com");
        signUpDto3.setNickname("ccc");
        Long memberId3 = memberService.signupLocal(signUpDto3);
        Member findMember3 = memberService.findById(memberId3);

        followService.save(findMember1.getId(), findMember2.getId());
        followService.save(findMember2.getId(), findMember1.getId());
        followService.save(findMember3.getId(), findMember1.getId());

        log.info("memberService.deleteMember() start");
        memberService.deleteMember(memberId);
        log.info("memberService.deleteMember() end");

        findMember1 = memberService.findById(memberId);
        List<Member> followingMembers = memberService.findFollowingMembers(findMember1.getId());
        List<Member> membersFollowingToMe = memberService.findMembersFollowingToMe(findMember1.getId());
        assertThat(findMember1.getIsDeleted()).isEqualTo(1);
        assertThat(followingMembers.size()).isEqualTo(0);
        assertThat(membersFollowingToMe.size()).isEqualTo(0);
    }

    private SignUpDto createSignUpDto() {
        return new SignUpDto(
                "user@aaa.com",
                "pass123!",
                "pass123!",
                "user",
                "address~",
                "I love my body~"
        );
    }
}
