package me.hwanse.chatserver.chatroom.controller;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.dto.ChatRoomDto;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ChatRoomDto createChatRoom(@RequestBody Map<String, String> input) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(input.get("title"));
        return new ChatRoomDto(chatRoom);
    }

    @GetMapping
    public List<ChatRoomDto> getAllChatRooms() {
        return chatRoomService.findAllChatRooms().stream()
                .map(ChatRoomDto::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ChatRoomDto getChatRoom(@PathVariable Long id) {
        return new ChatRoomDto(chatRoomService.findChatRoomById(id));
    }

}
