package com.modu.MemberServer.repository;

import com.modu.MemberServer.entity.Member;
import com.modu.MemberServer.entity.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndSocialType(String email, SocialType socialType);

    @Query(nativeQuery = true, value = "SELECT * FROM Member as m WHERE m.member_id IN " +
            "(SELECT f.TO_MEMBER FROM Follow f WHERE f.FROM_MEMBER = :memberId)")
    List<Member> findFollowingMembers(@Param("memberId") Long memberId);

    @Query(nativeQuery = true, value = "SELECT * FROM Member as m WHERE m.member_id IN " +
            "(SELECT f.FROM_MEMBER FROM Follow f WHERE f.TO_MEMBER = :memberId)")
    List<Member> findMembersFollowingToMe(@Param("memberId") Long memberId);
}
