package com.modu.ChatServer.controller;

import com.modu.ChatServer.client.ChatRoom;
import com.modu.ChatServer.client.ChatRoomRepository;
import com.modu.ChatServer.domain.ChatMessage;
import com.modu.ChatServer.repository.ChatMessageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * 채팅방 개설 (Json 형식 전달)
     */
    @PostMapping("/rooms")
    public String createRoom(@RequestBody ChatRoom room) {
        // JSON 형식으로 roomId, name 전달해서 POST 요청하면 ChatRoom 객체 생성
        chatRoomRepository.save(room);
        log.info("JSON 요청 : {}", room);
        return "redirect:/chat";
    }

    // room id로 채팅방 입장
    @GetMapping("/{roomId}")
    public String getRoom(@PathVariable String roomId, Model model) {
        ChatRoom room = chatRoomRepository.findRoomById(roomId);

        // 이전 채팅 기록 TODO : html 코드 수정하기
        List<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId);
        model.addAttribute("messages", messages);

        model.addAttribute("room", room);
        model.addAttribute("username", UUID.randomUUID().toString());
        // TODO : 실제 사용자 닉네임과 연동 생각하기. 현재는 입장할 때 username이 랜덤으로 생성됨
        return "room";
    }

}
