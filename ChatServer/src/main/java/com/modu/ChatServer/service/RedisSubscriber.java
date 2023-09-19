package com.modu.ChatServer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modu.ChatServer.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageTemplate;

    /**
     * Redis에서 메시지 발행되면 아래 onMessage 콜백 메서드에서 메시지를 받아 처리
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("redis로 발행된 메시지 {}", message);

            // ChatMessage 객체로 매핑
            ChatMessage chatMessage = objectMapper.readValue(message.toString(), ChatMessage.class);

            // WebSocket Session으로 roomId로 나눠서 메시지 push
            messageTemplate.convertAndSend("/topic/chat/room/" + chatMessage.getRoomId(), chatMessage);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
