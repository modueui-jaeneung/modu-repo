package com.modu.MemberServer.controller;


import com.modu.MemberServer.entity.Member;
import com.modu.MemberServer.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/{memberId}/profile")
    public ResponseEntity<ProfileResponseDto> getMemberProfile(@PathVariable Long memberId) {
        Member findMember = memberService.findByIdAndSetFollow(memberId);
        ProfileResponseDto profileResponseDto = new ProfileResponseDto(
                findMember.getNickname(),
                findMember.getAddress(),
                findMember.getFollowed().size(),
                findMember.getFollowing().size(),
                findMember.getIntroduceMyself());
        return new ResponseEntity(profileResponseDto, HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    public class ProfileResponseDto {
        String nickname;
        String address;
        int followedCount;
        int followingCount;
        String introduceMyself;
    }
}
