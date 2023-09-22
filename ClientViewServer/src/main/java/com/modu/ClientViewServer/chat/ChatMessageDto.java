package com.modu.ClientViewServer.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {

    private String roomId;
    private String writer;
    private String message;
    private LocalDateTime createdAt;
}
