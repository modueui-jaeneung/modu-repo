package com.modu.ChatServer.controller;

import com.modu.ChatServer.domain.ChatMessage;
import com.modu.ChatServer.domain.ChatRoom;
import com.modu.ChatServer.repository.ChatMessageRepository;
import com.modu.ChatServer.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    
    // 채팅방 생성 (MongoDB)
    @PostMapping("/rooms")
    public String createRoom(@RequestBody ChatRoom room) {
        chatRoomRepository.save(room);
        return "redirect:/";
    }

    // 모든 채팅방 조회
    @GetMapping
    public String rooms(Model model) {
        List<ChatRoom> rooms = chatRoomRepository.findAll();
        model.addAttribute("items", rooms);
        return "rooms";
    }

    // Room Id로 채팅방 조회
    @GetMapping("/{roomId}")
    public String getRoom(@PathVariable String roomId, Model model) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(NullPointerException::new);

        // 이전 채팅 기록 TODO : html 코드 수정하기
        List<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId);
        model.addAttribute("messages", messages);

        model.addAttribute("room", room);
        model.addAttribute("username", UUID.randomUUID().toString());
        // TODO : 실제 사용자 닉네임과 연동 생각하기. 현재는 입장할 때 username이 랜덤으로 생성됨
        return "room";
    }


    // 채팅방 삭제
}
