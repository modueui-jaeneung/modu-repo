package com.modu.ChatServer.controller;

import com.modu.ChatServer.domain.ChatMessage;
import com.modu.ChatServer.dto.ChatMessageDto;
import com.modu.ChatServer.repository.ChatMessageRepository;
import com.modu.ChatServer.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * WebSocket Handler의 역할을 아래 Controller 가 대신한다
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    // 특정 Broker 로 메세지를 전달 (@SendTo 대신 사용)
    private final SimpMessagingTemplate template;
    private final RedisPublisher redisPublisher;

    // TODO : 입장 메시지 대신 채팅 날짜로 ... (메시지 형태는 조금 다르게)
    @MessageMapping(value = "/chat/enter") // 클라이언트(브라우저)에서 메시지를 보내는 경로
    public void enter(ChatMessageDto message) {
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }

    // 채팅 메시지 처리
    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message) {
        redisPublisher.publish(message);
    }

}
