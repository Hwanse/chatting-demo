package me.hwanse.chatserver.chatroom.repository;

import me.hwanse.chatserver.chatroom.ChatVisitor;

import java.util.List;
import java.util.Optional;

public interface ChatVisitorCustomRepository {

    List<ChatVisitor> findChatVisitorsByChatRoomId(Long roomId);

    Optional<ChatVisitor> findVisitorByChatRoomIdAndSessionId(Long roomId, String sessionId);

}
