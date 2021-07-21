package me.hwanse.chatserver.chatroom.service;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.ChatVisitor;
import me.hwanse.chatserver.chatroom.repository.ChatRoomRepository;
import me.hwanse.chatserver.chatroom.repository.ChatVisitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatVisitorService {

    private final ChatVisitorRepository chatVisitorRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatVisitor addChatVisitor(Long roomId, String sessionId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(IllegalArgumentException::new);
        chatRoom.increaseUserCount();
        return chatVisitorRepository.save(new ChatVisitor(chatRoom, sessionId));
    }

    @Transactional
    public void leaveChatVisitor(String sessionId) {
        ChatVisitor chatVisitor = chatVisitorRepository.findBySessionId(sessionId);
        Long roomId = chatVisitor.getChatRoom().getId();
        chatVisitorRepository.delete(chatVisitor);
        chatRoomRepository.findById(roomId).ifPresent(ChatRoom::decreaseUserCount);
    }

}
