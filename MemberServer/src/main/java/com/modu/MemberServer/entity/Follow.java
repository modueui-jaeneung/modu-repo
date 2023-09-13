package com.modu.MemberServer.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "Follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromMember")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toMember")
    private Member toMember;

    private LocalDateTime createdAt;

    public Follow() { }

    // 생성 메서드
    public static Follow createFollow (
            Member fromMember,
            Member toMember
    ) {
        Follow follow = new Follow();
        follow.setFromMember(fromMember);
        follow.setToMember(toMember);
        follow.createdAt = LocalDateTime.now();
        return follow;
    }

    public void setFromMember(Member member) {
        this.fromMember = member;
        if (!member.getFollowing().contains(this)) {
            member.getFollowing().add(this);
        }
    }

    public void setToMember(Member member) {
        this.toMember = member;
        if (!member.getFollowed().contains(this)) {
            member.getFollowed().add(this);
        }
    }
}
