package com.modu.MemberServer.service;

import com.modu.MemberServer.dto.SignUpDto;
import com.modu.MemberServer.dto.UpdateMemberDto;
import com.modu.MemberServer.entity.Follow;
import com.modu.MemberServer.entity.Member;
import com.modu.MemberServer.entity.enums.SocialType;
import com.modu.MemberServer.exception.DuplicateMemberException;
import com.modu.MemberServer.exception.EmailFormatNotSatisfiedException;
import com.modu.MemberServer.exception.PasswordNotEqualException;
import com.modu.MemberServer.exception.PasswordNotSatisfiedException;
import com.modu.MemberServer.repository.FollowRepository;
import com.modu.MemberServer.repository.MemberRepository;
import com.modu.MemberServer.utils.EncryptHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    private final EncryptHelper encryptHelper;

    @Transactional
    public Long signupLocal(SignUpDto signUpDto) {

        // password 와 repeatPassword 가 맞는지 물론 화면 단에서 확인하고 요청했겠지만
        // 서버단에서도 재확인하기
        if (!signUpDto.getPassword().equals(signUpDto.getRepeatPassword())) {
            throw new PasswordNotEqualException("비밀번호가 일치하지 않습니다.");
        }

        String email = signUpDto.getEmail();
        // 이메일 중복 검사
        if (memberRepository.findByEmailAndSocialType(email, SocialType.LOCAL).isPresent()) {
            throw new DuplicateMemberException("이미 존재하는 회원입니다.");
        }

        // 이메일 형식 검사
        if (!email.matches("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            throw new EmailFormatNotSatisfiedException("이메일 형식이 올바르지 않습니다. 올바른 형식: 계정@도메인.최상위도메인");
        }

        String password = signUpDto.getPassword();
        // 비밀번호 유효성 검사
        // 최소 8자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$")) {
            throw new PasswordNotSatisfiedException("비밀번호가 유효하지 않습니다. 최소 8자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자가 들어있어야 합니다.");
        }

        // TODO 이메일 인증하기

        Member member = new Member(
                signUpDto.getEmail(),
                SocialType.LOCAL,
                encryptHelper.encrypt(signUpDto.getPassword()),
                signUpDto.getNickname(),
                signUpDto.getAddress(),
                signUpDto.getIntroduceMyself());
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    public Member findByEmailAndSocialType(String email, SocialType socialType) {
        Member member = memberRepository.findByEmailAndSocialType(email, socialType).orElseThrow(() -> new NoSuchElementException("(이메일,소셜타입)과 일치하는 회원이 없습니다."));
        return member;
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow();
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    // 내가 팔로우 한 사람들 조회하기
    public List<Member> findFollowingMembers(Long memberId) {
        return memberRepository.findFollowingMembers(memberId);
    }

    // 나를 팔로우 한 사람들 조회하기 (내가 받는 쪽)
    public List<Member> findMembersFollowingToMe(Long memberId) {
        return memberRepository.findMembersFollowingToMe(memberId);
    }

    // 회원수정
    @Transactional
    public Long updateMember(Long memberId, UpdateMemberDto updateMemberDto) {

        // password 와 repeatPassword 가 맞는지 물론 화면 단에서 확인하고 요청했겠지만
        // 서버단에서도 재확인하기
        if (!updateMemberDto.getPassword().equals(updateMemberDto.getRepeatPassword())) {
            throw new PasswordNotEqualException("비밀번호가 일치하지 않습니다.");
        }

        String password = updateMemberDto.getPassword();
        // 비밀번호 유효성 검사
        // 최소 8자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$")) {
            throw new PasswordNotSatisfiedException("비밀번호가 유효하지 않습니다. 최소 8자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자가 들어있어야 합니다.");
        }

        Member member = findById(memberId);
        Member updateMember = member.updateMember(
                updateMemberDto.getNickname(),
                updateMemberDto.getAddress(),
                encryptHelper.encrypt(updateMemberDto.getPassword()),
                updateMemberDto.getIntroduceMyself());
        return updateMember.getId();
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findById(memberId);
        // 팔로우 관계 지우기
//        member.getFollowed().clear(); // 이런다고 해서 follow 테이블에 있는 데이터가 알아서 지워지지 않음
//        member.getFollowing().clear();

        List<Follow> followingList = member.getFollowing();
        List<Follow> followedList = member.getFollowed();
        followingList.addAll(followedList);
        followRepository.deleteAllInBatch(followingList);
        member.delete();
    }
}