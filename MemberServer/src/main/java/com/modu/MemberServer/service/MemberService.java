package com.modu.MemberServer.service;

import com.modu.MemberServer.entity.Member;
import com.modu.MemberServer.entity.enums.SocialType;
import com.modu.MemberServer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        Member savedMember = memberRepository.save(member);
        return savedMember.getMemberId();
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow();
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findByEmailAndSocialType(String email, SocialType socialType) {
        return memberRepository.findByEmailAndSocialType(email, socialType).orElseThrow();
    }


    public Member findByIdAndSetFollow(Long memberId) {
        Member findMember = findById(memberId);
        findMember.getFollowed();
        findMember.getFollowing();
        return findMember;
    }


}
