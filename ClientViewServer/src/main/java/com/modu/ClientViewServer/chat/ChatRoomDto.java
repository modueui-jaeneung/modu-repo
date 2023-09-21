package com.modu.ClientViewServer.chat;

import lombok.Data;

@Data
public class ChatRoomDto {

    private String roomId;
    private String ownerId;
    private String title;

}
