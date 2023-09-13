package com.modu.MemberServer.service;

import com.modu.MemberServer.entity.Follow;
import com.modu.MemberServer.entity.Member;
import com.modu.MemberServer.exception.EmptyMemberException;
import com.modu.MemberServer.exception.NoFollowException;
import com.modu.MemberServer.exception.SameMemberException;
import com.modu.MemberServer.repository.FollowRepository;
import com.modu.MemberServer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(Long fromMemberId, Long toMemberId) {
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID입니다."));
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 같은 회원일 경우 예외 던지기
        if (fromMemberId.equals(toMemberId)) {
            throw new SameMemberException("같은 회원입니다.");
        }

        // 이미 from -> to 로의 팔로우 관계가 있는 경우 예외 던지기
        Optional<Follow> followOptional = followRepository.findByFromMemberAndToMember(fromMember, toMember);
        if (followOptional.isPresent()) {
            throw new IllegalStateException("이미 존재하는 팔로우 관계입니다.");
        }

        Follow follow = Follow.createFollow(fromMember, toMember);
        Follow savedFollow = followRepository.save(follow);
        return savedFollow.getId();
    }

    public Follow findById(Long followId) {
        return followRepository.findById(followId).orElseThrow();
    }

    @Transactional
    public void deleteFollow(Long fromMemberId, Long toMemberId) {
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID입니다."));
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 같은 회원일 경우 예외 던지기
        if (fromMemberId.equals(toMemberId)) {
            throw new SameMemberException("같은 회원입니다.");
        }

        Follow follow = followRepository.findByFromMemberAndToMember(fromMember, toMember).orElseThrow();

        followRepository.delete(follow);
    }
}
