package me.hwanse.chatserver.chatroom.repository;

import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.ChatVisitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatVisitorRepository extends JpaRepository<ChatVisitor, Long> {

    ChatVisitor findBySessionId(String sessionId);

}
