package com.modu.ClientViewServer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateMemberDto {

    String email;
    String password;
    String repeatPassword;
    String nickname;
    String address;
    String introduceMyself;
}
