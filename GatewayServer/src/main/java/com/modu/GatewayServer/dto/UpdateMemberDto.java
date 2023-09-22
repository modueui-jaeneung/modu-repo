package com.modu.GatewayServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMemberDto {
    String email;
    String password;
    String repeatPassword;
    String nickname;
    String address;
    String introduceMyself;
}
