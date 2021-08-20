package me.hwanse.chatserver.chatroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.api.ApiResult;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.dto.ChatRoomDto;
import me.hwanse.chatserver.chatroom.dto.ChatRoomDtoToModelConverter;
import me.hwanse.chatserver.chatroom.dto.CreateChatRoomRequest;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static me.hwanse.chatserver.api.ApiResult.Response;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/chat-room", produces = MediaTypes.HAL_JSON_VALUE)
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomDtoToModelConverter converter;

    @PostMapping
    public ResponseEntity createChatRoom(@RequestBody @Valid CreateChatRoomRequest createRequest, @AuthenticationPrincipal String userId) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(createRequest.getTitle(), createRequest.getLimitUserCount(), userId);
        EntityModel<ChatRoomDto> model = converter.toModel(new ChatRoomDto(chatRoom, userId));
        model.add(linkTo(ChatRoomController.class).withRel("query-chatrooms"));
        model.add(linkTo(ChatRoomController.class).slash(chatRoom.getId()).withRel("disable-chatroom"));
        model.add(Link.of("/docs/index.html#chatroom-create").withRel("profile"));

        return ResponseEntity
                .created(model.getLink(IanaLinkRelations.SELF).get().toUri())
                .body(Response(model));
    }

    @GetMapping
    public ResponseEntity getAllChatRooms(@AuthenticationPrincipal String userId) {
        List<ChatRoomDto> chatRooms = chatRoomService.findAllChatRooms().stream()
                .map(chatRoom -> new ChatRoomDto(chatRoom, userId)).collect(Collectors.toList());
        return ResponseEntity
                .ok(
                        Response(converter.toCollectionModel(chatRooms))
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity getChatRoom(@PathVariable Long id, @AuthenticationPrincipal String userId) {
        ChatRoomDto roomDto = new ChatRoomDto(chatRoomService.findChatRoomById(id), userId);
        EntityModel<ChatRoomDto> model = converter.toModel(roomDto);
        model.add(Link.of("/docs/index.html#chatroom-info").withRel("profile"));
        if (roomDto.isMeManager()) {
            model.add(linkTo(ChatRoomController.class).slash(roomDto.getId()).withRel("disable-chatroom"));
        }

        return ResponseEntity
                .ok(Response(model));
    }

    @PatchMapping("/{id}")
    public ApiResult disableChatRoom(@PathVariable Long id, @AuthenticationPrincipal String userId) {
        chatRoomService.disableChatRoom(id, userId);
        HashMap<String, Link> responseDataMap = new HashMap<>();
        responseDataMap.put("links", Link.of("/docs/index.html#chatroom-disable").withRel("profile"));
        return Response(responseDataMap);
    }

}
