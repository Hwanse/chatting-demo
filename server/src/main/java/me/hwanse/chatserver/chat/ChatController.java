package me.hwanse.chatserver.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.chat.text.ChatMessage;
import me.hwanse.chatserver.chat.voice.IceMessage;
import me.hwanse.chatserver.chat.voice.LeaveMessage;
import me.hwanse.chatserver.chat.voice.SdpMessage;
import me.hwanse.chatserver.chat.voice.VisitorMessage;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.dto.ChatVisitorDto;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import me.hwanse.chatserver.chatroom.service.ChatVisitorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@MessageMapping("/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final ChatVisitorService chatVisitorService;

    // =========== 채팅방 공통 ==============
    @MessageMapping("/join")
    public void joinInChat(SimpMessageHeaderAccessor headerAccessor, @Valid ChatMessage chatMessage) {
//        chatMessage.setSessionId(headerAccessor.getSessionId());
        messagingTemplate.convertAndSend(getDestination(chatMessage.getRoomId()), chatMessage);
    }

    // =========== 텍스트 채팅 관련 ==============
    @MessageMapping("/text/message")
    public void sendMessage(@Valid ChatMessage chatMessage) {
        messagingTemplate.convertAndSend(getDestination(chatMessage.getRoomId()), chatMessage);
    }

//    @MessageMapping("/text/monitoring")
//    public void monitoringRoom(@Valid ChatMessage chatMessage) {
//        ChatRoom chatRoom = chatRoomService.findChatRoomById(chatMessage.getRoomId());
//        chatMessage.setUserCount(chatRoom.getUserCount());
//        messagingTemplate.convertAndSend(getDestination(chatMessage.getRoomId()), chatMessage);
//    }

    // =========== 음성 채팅 관련 ==============
    @MessageMapping("/voice/visitors")
    public void findVisitors(SimpMessageHeaderAccessor headerAccessor, @Valid @Payload(required = true) Long roomId) {
        List<ChatVisitorDto> visitors = chatVisitorService.findVisitorsInTheChatRoom(roomId).stream()
                .map(ChatVisitorDto::new).collect(Collectors.toList());
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(),
                getDestination(roomId) + "/visitors", new VisitorMessage(roomId, visitors));
    }

    @MessageMapping("/voice/offer")
    public void sendVoiceToPeers(@Valid SdpMessage message) {
        messagingTemplate.convertAndSendToUser(message.getToId(), getDestination(message.getRoomId()) + "/voice", message);
    }

    @MessageMapping("/voice/candidate")
    public void sendVoiceToPeers(@Valid IceMessage message) {
        messagingTemplate.convertAndSendToUser(message.getToId(), getDestination(message.getRoomId()) +"/voice", message);
    }

    @MessageMapping("/voice/leave")
    public void leaveChatNotification(@Valid LeaveMessage leaveMessage) {
        chatVisitorService.leaveChatVisitorInChatRoom(leaveMessage.getRoomId(), leaveMessage.getSessionId());
        messagingTemplate.convertAndSend(getDestination(leaveMessage.getRoomId()) + "/leave", leaveMessage.getSessionId());
    }

    private String getDestination(Long roomId) {
        return String.format("/sub/chat-room/%d", roomId);
    }

    @MessageExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class,
            IllegalStateException.class, HttpMediaTypeException.class})
    public ResponseEntity<?> handleSocketBadRequestException(Exception e) {
        log.debug("socket bad request exception occurred : {}", e.getMessage(), e);
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(e.getMessage());
    }

}
