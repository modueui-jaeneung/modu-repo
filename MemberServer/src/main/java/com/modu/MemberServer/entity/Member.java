package com.modu.MemberServer.entity;

import com.modu.MemberServer.entity.enums.SocialType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @NotNull
    private String password;

    @NotNull
    private String nickname;

    @NotNull
    private String address;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    private String introduceMyself;

    @OneToMany(mappedBy = "follow_id", fetch = FetchType.LAZY)
    private List<Follow> followed = new ArrayList<>();

    @OneToMany(mappedBy = "follow_id", fetch = FetchType.LAZY)
    private List<Follow> following = new ArrayList<>();

    private int isDeleted;

    public Member() {}

    // 생성 메서드
    public Member(
            String email,
            SocialType socialType,
            String password,
            String nickname,
            String address,
            String introduceMyself

    ) {
        Member member = new Member();
        member.email = email;
        member.socialType = socialType;
        member.password = password;
        member.nickname = nickname;
        member.address = address;
        LocalDateTime now = LocalDateTime.now();
        member.createdAt = now;
        member.updatedAt = now;
        member.introduceMyself = introduceMyself;
        member.isDeleted = 0;
        member.followed = new ArrayList<>();
        member.following = new ArrayList<>();
    }
}
