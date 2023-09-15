package com.modu.ChatServer.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅방 목록 조회 (실제 우리 서비스에서 게시글 아래에 보일 채팅방 목록 정보 조회)
     */
    @GetMapping
    public String rooms(Model model) {
        List<ChatRoom> rooms = chatRoomRepository.findAllRooms();
        model.addAttribute("items", rooms);
        return "rooms";
    }

    /**
     * 테스트용 데이터
     */
    @PostConstruct
    public void init() {
        chatRoomRepository.createChatRoom("room1");
    }
}
