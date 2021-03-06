package me.hwanse.chatserver.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.chat.text.ChatMessage;
import me.hwanse.chatserver.chat.text.MessageType;
import me.hwanse.chatserver.chat.text.MonitoringMessage;
import me.hwanse.chatserver.chat.voice.IceMessage;
import me.hwanse.chatserver.chat.voice.LeaveMessage;
import me.hwanse.chatserver.chat.voice.SdpMessage;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import me.hwanse.chatserver.chatroom.service.ChatVisitorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.Valid;

import static me.hwanse.chatserver.chat.text.MessageType.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@MessageMapping("/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatVisitorService chatVisitorService;

    // =========== 채팅방 공통 ===========
    @MessageMapping("/join")
    public void joinInChat(@Valid ChatMessage chatMessage) {
        messagingTemplate.convertAndSend(getDestination(chatMessage.getRoomId(), JOIN), chatMessage);
    }

    @MessageMapping("/monitoring")
    public void monitoringUserCountInChatRoom(SimpMessageHeaderAccessor headerAccessor, @Valid MonitoringMessage message) {
        message.setUserCount(chatVisitorService.findVisitorsInTheChatRoom(message.getRoomId()).size());
        messagingTemplate.convertAndSendToUser(
                headerAccessor.getSessionId(),
                getDestination(message.getRoomId(), MONITORING),
                message
        );
    }

    // =========== 텍스트 채팅 관련 ===========
    @MessageMapping("/text/message")
    public void sendMessage(@Valid ChatMessage chatMessage) {
        messagingTemplate.convertAndSend(getDestination(chatMessage.getRoomId(), TALK), chatMessage);
    }

    // =========== 음성 채팅 관련 ===========
    @MessageMapping("/voice/offer")
    public void sendVoiceToPeers(@Valid SdpMessage message) {
        messagingTemplate.convertAndSendToUser(message.getToId(), getDestination(message.getRoomId(), VOICE), message);
    }

    @MessageMapping("/voice/candidate")
    public void sendVoiceToPeers(@Valid IceMessage message) {
        messagingTemplate.convertAndSendToUser(message.getToId(), getDestination(message.getRoomId(), VOICE), message);
    }

    @MessageMapping("/voice/leave")
    public void leaveChatNotification(@Valid LeaveMessage leaveMessage) {
        chatVisitorService.leaveChatVisitorInChatRoom(leaveMessage.getRoomId(), leaveMessage.getSessionId());
        messagingTemplate.convertAndSend(getDestination(leaveMessage.getRoomId(), LEAVE), leaveMessage.getSessionId());
    }

    private String getDestination(Long roomId, MessageType messageType) {
        return String.format("/sub/chat-room/%d%s", roomId, messageType.getDestination());
    }

    @MessageExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class,
            IllegalStateException.class, HttpMediaTypeException.class})
    public ResponseEntity<?> handleSocketBadRequestException(Exception e) {
        log.debug("socket bad request exception occurred : {}", e.getMessage(), e);
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(e.getMessage());
    }

}
