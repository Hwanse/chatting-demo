package me.hwanse.chatserver.chatroom.service;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.ChatVisitor;
import me.hwanse.chatserver.chatroom.repository.ChatRoomRepository;
import me.hwanse.chatserver.chatroom.repository.ChatVisitorRepository;
import me.hwanse.chatserver.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ChatVisitorService {

    private final ChatVisitorRepository chatVisitorRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = true)
    public List<ChatVisitor> findVisitorsInTheChatRoom(Long roomId) {
        return chatVisitorRepository.findChatVisitorsByChatRoomId(roomId);
    }

    @Transactional
    public ChatVisitor addChatVisitor(Long roomId, String sessionId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new NotFoundException(ChatRoom.class, roomId));
        return chatVisitorRepository.findVisitorByChatRoomIdAndSessionId(roomId, sessionId)
                .orElseGet(() -> {
                    chatRoom.increaseUserCount();
                    return chatVisitorRepository.save(new ChatVisitor(chatRoom, sessionId));
                });
    }

    @Transactional
    public void leaveChatVisitor(String sessionId) {
        List<ChatVisitor> chatVisitors = chatVisitorRepository.findBySessionId(sessionId);

        for (ChatVisitor visitor : chatVisitors) {
            visitor.getChatRoom().decreaseUserCount();
        }
        chatVisitorRepository.deleteAll(chatVisitors);
    }

    @Transactional
    public void leaveChatVisitorInChatRoom(Long roomId, String sessionId) {
        chatVisitorRepository.findByChatRoomIdAndSessionId(roomId, sessionId)
                .ifPresent(chatVisitor -> {
                    chatVisitor.getChatRoom().decreaseUserCount();
                    chatVisitorRepository.delete(chatVisitor);
                });
    }

}
