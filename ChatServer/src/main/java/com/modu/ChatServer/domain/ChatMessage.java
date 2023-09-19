package com.modu.ChatServer.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter @Setter
@Document(collection = "message")
public class ChatMessage {

    @Id
    @Field("_id")
    private String id;

    @Field("room_id")
    private String roomId;
    private String writer;
    private String message;

    // TODO: LocalDateTime 필드를 유지하기 위해, 나중에 MessageDto 추가하기
//    @CreatedDate
//    @Field("created_at")
//    private LocalDateTime createdAt;
}
