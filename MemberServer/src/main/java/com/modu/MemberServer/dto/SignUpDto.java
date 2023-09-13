package com.modu.MemberServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpDto {

    String email;
    String password;
    String repeatPassword;
    String nickname;
    String address;
}
