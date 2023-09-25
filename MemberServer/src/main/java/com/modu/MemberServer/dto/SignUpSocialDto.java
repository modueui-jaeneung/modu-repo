package com.modu.MemberServer.dto;

import com.modu.MemberServer.entity.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpSocialDto {

    String email;
    String socialType;
    String nickname;
}
