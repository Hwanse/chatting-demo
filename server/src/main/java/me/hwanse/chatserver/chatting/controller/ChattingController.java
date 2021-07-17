package me.hwanse.chatserver.chatting.controller;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatting.ChatRoom;
import me.hwanse.chatserver.chatting.dto.ChatRoomDto;
import me.hwanse.chatserver.chatting.service.ChattingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chattings")
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;

    @PostMapping
    public ChatRoomDto createChatRoom(String title) {
        ChatRoom chatRoom = chattingService.createChatRoom(title);
        return new ChatRoomDto(chatRoom);
    }

    @GetMapping
    public List<ChatRoomDto> getChatRooms() {
        return chattingService.findAllChatRooms().stream()
                .map(ChatRoomDto::new).collect(Collectors.toList());
    }

}
