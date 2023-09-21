package com.modu.ChatServer.dto;

import com.modu.ChatServer.domain.ChatMessage;
import lombok.Data;

@Data
public class ChatMessageDto {

    private String roomId;
    private String writer;
    private String message;

    public ChatMessageDto(String roomId, String writer, String message) {
        this.roomId = roomId;
        this.writer = writer;
        this.message = message;
    }

    public ChatMessageDto(ChatMessage chatMessage) {
        this.roomId = chatMessage.getRoomId();
        this.writer = chatMessage.getWriter();
        this.message = chatMessage.getMessage();
    }

}
