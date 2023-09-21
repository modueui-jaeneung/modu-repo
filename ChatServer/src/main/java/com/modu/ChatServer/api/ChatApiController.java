package com.modu.ChatServer.api;

import com.modu.ChatServer.domain.ChatRoom;
import com.modu.ChatServer.dto.ChatMessageDto;
import com.modu.ChatServer.dto.ChatRoomDto;
import com.modu.ChatServer.repository.ChatMessageRepository;
import com.modu.ChatServer.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatApiController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        chatRoomRepository.save(chatRoomDto.toEntity());
        return new ResponseEntity<ChatRoomDto>(chatRoomDto, HttpStatus.CREATED);
    }

    // 모든 채팅방 데이터 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDto>> rooms() {
        List<ChatRoomDto> chatRoomDtoList = chatRoomRepository.findAll().stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());

        return new ResponseEntity<List<ChatRoomDto>>(chatRoomDtoList, HttpStatus.OK);
    }

    // 특정 채팅방 데이터 조회
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ChatRoomDto> room(@PathVariable String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(NullPointerException::new);
        ChatRoomDto chatRoomDto = new ChatRoomDto(room);

        return new ResponseEntity<ChatRoomDto>(chatRoomDto, HttpStatus.OK);
    }

    // 특정 채팅방 전체 메시지 조회
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> messages(@PathVariable String roomId) {
        List<ChatMessageDto> chatMessageDtoList = chatMessageRepository.findByRoomId(roomId).stream()
                .map(ChatMessageDto::new)
                .collect(Collectors.toList());

        return new ResponseEntity<List<ChatMessageDto>>(chatMessageDtoList, HttpStatus.OK);
    }
}
