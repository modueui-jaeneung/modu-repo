package com.modu.ChatServer.dto;

import com.modu.ChatServer.domain.ChatRoom;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatRoomDto {

    private String roomId;
    private String ownerId;
    private String title;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.ownerId = chatRoom.getOwnerId();
        this.title = chatRoom.getTitle();
    }

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .roomId(roomId)
                .ownerId(ownerId)
                .title(title)
                .build();
    }
}
