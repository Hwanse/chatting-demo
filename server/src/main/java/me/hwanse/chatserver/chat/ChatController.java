package me.hwanse.chatserver.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.chat.text.ChatMessage;
import me.hwanse.chatserver.chat.voice.IceMessage;
import me.hwanse.chatserver.chat.voice.JoinMessage;
import me.hwanse.chatserver.chat.voice.SdpMessage;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.dto.ChatVisitorDto;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import me.hwanse.chatserver.chatroom.service.ChatVisitorService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final ChatVisitorService chatVisitorService;

    // =========== 텍스트 채팅 관련 ==============
    @MessageMapping("/chat/join")
    public void joinInChat(ChatMessage chatMessage) {
        messagingTemplate.convertAndSend(getDestination(chatMessage.getRoomId()), chatMessage);
    }

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessage chatMessage) {
        messagingTemplate.convertAndSend(getDestination(chatMessage.getRoomId()), chatMessage);
    }

    @MessageMapping("/chat/monitoring")
    public void monitoringRoom(ChatMessage chatMessage) {
        ChatRoom chatRoom = chatRoomService.findChatRoomById(chatMessage.getRoomId());
        chatMessage.setUserCount(chatRoom.getUserCount());
        messagingTemplate.convertAndSend(getDestination(chatMessage.getRoomId()), chatMessage);
    }

    // =========== 음성 채팅 관련 ==============
    @MessageMapping("/chat/visitors")
    public void findVisitors(SimpMessageHeaderAccessor headerAccessor) {
        List<ChatVisitorDto> visitors = chatVisitorService.findVisitorsInTheChatRoom(1L).stream()
                .map(ChatVisitorDto::new).collect(Collectors.toList());
        messagingTemplate.convertAndSend("/sub/chat-room/1/visitors", new JoinMessage(visitors));
    }

    @MessageMapping("/chat/offer")
    public void sendVoiceToPeers(SdpMessage message) {
        messagingTemplate.convertAndSend("/sub/chat-room/1/voice", message);
    }

    @MessageMapping("/chat/candidate")
    public void sendVoiceToPeers(IceMessage message) {
        messagingTemplate.convertAndSend("/sub/chat-room/1/voice", message);
    }

    private String getDestination(Long roomId) {
        return String.format("/sub/chat-room/%d", roomId);
    }

}
