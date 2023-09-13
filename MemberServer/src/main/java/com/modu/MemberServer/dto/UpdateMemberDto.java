package com.modu.MemberServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateMemberDto {
    String nickname;
    String address;
    String password;
    String repeatPassword;
    String introduceMyself;
}
