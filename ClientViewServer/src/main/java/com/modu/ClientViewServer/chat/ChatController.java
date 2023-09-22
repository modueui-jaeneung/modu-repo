package com.modu.ClientViewServer.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RestTemplate restTemplate;

    @GetMapping("/chat")
    public String chatIndex(Model model) {

        String url = "http://localhost:8085/chat/rooms";
        ResponseEntity<ChatRoomDto[]> response = restTemplate.getForEntity(url, ChatRoomDto[].class);

        ChatRoomDto[] rooms = response.getBody();
        model.addAttribute("rooms", rooms);

        return "chat";
    }

    @GetMapping("/chat/{roomId}")
    public String enterRoom(@PathVariable String roomId, Model model) {
        String url = "http://localhost:8085/chat";

        // 채팅방 목록 가져오기
        ResponseEntity<ChatRoomDto[]> responseRooms = restTemplate.getForEntity(url + "/rooms", ChatRoomDto[].class);

        ChatRoomDto[] rooms = responseRooms.getBody();
        model.addAttribute("rooms", rooms);

        // RoomID에 해당하는 채팅방 가져오기
        ChatRoomDto room = restTemplate.getForObject(url + "/rooms/" + roomId, ChatRoomDto.class);
        model.addAttribute("room", room);

//        // 메시지 가져오기
        ResponseEntity<ChatMessageDto[]> responseMessages =
                restTemplate.getForEntity(url + "/rooms/" + roomId + "/messages", ChatMessageDto[].class);

        ChatMessageDto[] messages = responseMessages.getBody();
        model.addAttribute("messages", messages);

        // TODO : 실제 사용자 닉네임과 연동 생각하기. 현재는 입장할 때 username이 랜덤으로 생성됨
        model.addAttribute("username", UUID.randomUUID().toString());

        return "room";
    }
}
