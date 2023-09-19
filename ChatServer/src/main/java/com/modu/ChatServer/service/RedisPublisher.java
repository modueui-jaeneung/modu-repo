package com.modu.ChatServer.service;

import com.modu.ChatServer.domain.ChatMessage;
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

    public void publish(ChatMessage message) {

        // Redis Channel로 메시지 전송
        log.info("redis 채널로 발송할 메시지 -> {}", message.getMessage());
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);

        // TODO : Mongo DB에 메시지 저장 로직을 여기에 넣을지
    }
}
