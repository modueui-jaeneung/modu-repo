package com.modu.MemberServer.repository;

import com.modu.MemberServer.entity.Member;
import com.modu.MemberServer.entity.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndSocialType(String email, SocialType socialType);
}
