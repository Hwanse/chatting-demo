package me.hwanse.chatserver.chatroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.api.ApiResult;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.dto.ChatRoomDto;
import me.hwanse.chatserver.chatroom.dto.CreateChatRoomRequest;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.hwanse.chatserver.api.ApiResult.OK;

@Slf4j
@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ApiResult createChatRoom(@RequestBody @Valid CreateChatRoomRequest createRequest, @AuthenticationPrincipal String userId) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(createRequest.getTitle(), createRequest.getLimitUserCount(), userId);
        return OK(chatRoom);
    }

    @GetMapping
    public ApiResult getAllChatRooms(@AuthenticationPrincipal String userId) {
        List<ChatRoomDto> chatRooms = chatRoomService.findAllChatRooms().stream()
                .map(chatRoom -> new ChatRoomDto(chatRoom, userId)).collect(Collectors.toList());
        return OK(chatRooms);
    }

    @GetMapping("/{id}")
    public ApiResult getChatRoom(@PathVariable Long id, @AuthenticationPrincipal String userId) {
        return OK(
                new ChatRoomDto(chatRoomService.findChatRoomById(id), userId)
        );
    }

}
