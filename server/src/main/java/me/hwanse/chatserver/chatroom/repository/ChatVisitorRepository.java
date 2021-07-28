package me.hwanse.chatserver.chatroom.repository;

import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.ChatVisitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatVisitorRepository extends JpaRepository<ChatVisitor, Long>, ChatVisitorCustomRepository {

    Optional<ChatVisitor> findBySessionId(String sessionId);

}
