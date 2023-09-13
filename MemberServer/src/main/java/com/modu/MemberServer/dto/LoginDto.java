package com.modu.MemberServer.dto;

import com.modu.MemberServer.entity.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {

    private String email;
    private SocialType socialType;
}
