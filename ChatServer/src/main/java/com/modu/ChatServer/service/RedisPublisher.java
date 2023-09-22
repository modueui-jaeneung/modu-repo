package com.modu.ChatServer.service;

import com.modu.ChatServer.domain.ChatMessage;
import com.modu.ChatServer.dto.ChatMessageDto;
import com.modu.ChatServer.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final ChatMessageRepository chatMessageRepository;

    public void publish(ChatMessageDto message) {

        // DB에 메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(message.getRoomId())
                .message(message.getMessage())
                .writer(message.getWriter())
                .build();

        chatMessageRepository.save(chatMessage);

        // Redis Channel로 메시지 전송
        log.info("redis 채널로 발송할 메시지 -> {}", message.getMessage());
        redisTemplate.convertAndSend(channelTopic.getTopic(), new ChatMessageDto(chatMessage));

        // TODO : Mongo DB에 메시지 저장 로직을 여기에 넣을지
    }
}
