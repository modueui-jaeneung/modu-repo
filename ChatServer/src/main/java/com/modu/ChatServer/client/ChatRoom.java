package com.modu.ChatServer.client;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class ChatRoom {

    private String roomId;
    private String name;
    private Set<WebSocketSession> socketSessions = new HashSet<>();

    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.setRoomId(UUID.randomUUID().toString());
        room.setName(name);
        return room;
    }
}
