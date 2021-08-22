package me.hwanse.chatserver.chatroom.repository;

import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.ChatVisitor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatVisitorRepository extends JpaRepository<ChatVisitor, Long>, ChatVisitorCustomRepository {

    @EntityGraph(attributePaths = "chatRoom", type = EntityGraph.EntityGraphType.FETCH)
    List<ChatVisitor> findBySessionId(String sessionId);

    @EntityGraph(attributePaths = "chatRoom", type = EntityGraph.EntityGraphType.FETCH)
    Optional<ChatVisitor> findByChatRoomIdAndSessionId(Long roomId, String sessionId);

}
