package com.modu.ChatServer.client;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatRoomRepository {
    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRooms() {
        // 채팅방 생성 순서 최근 순으로 반환
        ArrayList<ChatRoom> list = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(list);

        return list;
    }

    public ChatRoom findRoomById(String id) {
        return chatRoomMap.get(id);
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom room = ChatRoom.create(name);
        chatRoomMap.put(room.getRoomId(), room);
        return room;
    }

    public ChatRoom save(ChatRoom room) {
        chatRoomMap.put(room.getRoomId(), room);
        return room;
    }
}