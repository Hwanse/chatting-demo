package me.hwanse.chatserver.chat;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;

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

    private String getDestination(Long roomId) {
        return String.format("/sub/chat-room/%d", roomId);
    }

}
