package com.modu.ChatServer.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.modu.ChatServer.domain.ChatMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {

    private String roomId;
    private String writer;
    private String message;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    public ChatMessageDto(String roomId, String writer, String message) {
        this.roomId = roomId;
        this.writer = writer;
        this.message = message;
    }

    public ChatMessageDto(ChatMessage chatMessage) {
        this.roomId = chatMessage.getRoomId();
        this.writer = chatMessage.getWriter();
        this.message = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }

}
