package com.modu.MemberServer.entity;

import com.modu.MemberServer.dto.UpdateMemberDto;
import com.modu.MemberServer.entity.enums.SocialType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "fromMember", fetch = FetchType.LAZY)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "toMember", fetch = FetchType.LAZY)
    private List<Follow> followed = new ArrayList<>();


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
        this.email = email;
        this.socialType = socialType;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.introduceMyself = introduceMyself;
        this.isDeleted = 0;
    }

    public Member updateMember(
            String nickname,
            String address,
            String password,
            String introduceMyself
    ) {
        this.nickname = nickname;
        this.address = address;
        this.password = password;
        this.introduceMyself = introduceMyself;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public void delete() {
        this.isDeleted = 1;
    }
}
