package me.hwanse.chatserver.chatroom.repository;

import me.hwanse.chatserver.chatroom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByUseTrueOrderByCreatedAtDesc();

}
