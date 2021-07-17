package me.hwanse.chatserver.chatting.controller;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatting.ChatRoom;
import me.hwanse.chatserver.chatting.dto.ChatRoomDto;
import me.hwanse.chatserver.chatting.service.ChattingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chattings")
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;

    @PostMapping
    public ChatRoomDto createChatRoom(@RequestBody Map<String, String> jsonInput) {
        ChatRoom chatRoom = chattingService.createChatRoom(jsonInput.get("title"));
        return new ChatRoomDto(chatRoom);
    }

    @GetMapping
    public List<ChatRoomDto> getChatRooms() {
        return chattingService.findAllChatRooms().stream()
                .map(ChatRoomDto::new).collect(Collectors.toList());
    }

}
