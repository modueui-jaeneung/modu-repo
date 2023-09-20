package com.modu.ChatServer.repository;

import com.modu.ChatServer.domain.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
}
