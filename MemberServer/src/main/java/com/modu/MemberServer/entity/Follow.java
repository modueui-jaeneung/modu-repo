package com.modu.MemberServer.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following")
    private Member toMember;

    private LocalDateTime createdAt;

    public Follow() { }

    public Follow createFollow(
            Member fromMember,
            Member toMember
    ) {
        Follow follow = new Follow();
        follow.fromMember = fromMember;
        follow.toMember = toMember;
        follow.createdAt = LocalDateTime.now();
        fromMember.getFollowing().add(follow);
        toMember.getFollowed().add(follow);
        return follow;
    }
}
