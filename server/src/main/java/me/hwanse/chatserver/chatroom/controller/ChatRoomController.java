package me.hwanse.chatserver.chatroom.controller;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.api.ApiResult;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.dto.ChatRoomDto;
import me.hwanse.chatserver.chatroom.dto.CreateChatRoomRequest;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.hwanse.chatserver.api.ApiResult.OK;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ApiResult createChatRoom(@RequestBody @Valid CreateChatRoomRequest createRequest) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(createRequest.getTitle(), createRequest.getLimitUserCount());
        return OK(chatRoom);
    }

    @GetMapping
    public ApiResult getAllChatRooms() {
        List<ChatRoomDto> chatRooms = chatRoomService.findAllChatRooms().stream()
                .map(ChatRoomDto::new).collect(Collectors.toList());
        return OK(chatRooms);
    }

    @GetMapping("/{id}")
    public ApiResult getChatRoom(@PathVariable Long id) {
        return OK(
                new ChatRoomDto(chatRoomService.findChatRoomById(id))
        );
    }

}
