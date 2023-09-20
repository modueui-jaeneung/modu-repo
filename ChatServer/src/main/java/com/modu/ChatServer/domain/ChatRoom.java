package com.modu.ChatServer.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "room")
public class ChatRoom {

    @Id
    @Field("_id")
    private String roomId;

    @Field("owner_id")
    private String ownerId;
    private String title;

//    private String participantId; // TODO: 채팅방 참석자 (판매자)
}
