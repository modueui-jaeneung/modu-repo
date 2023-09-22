package com.modu.GatewayServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoViewDto {
    String email;
    String nickname;
    String address;
    String introduceMyself;
}
