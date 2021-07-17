package me.hwanse.chatserver.chatroom.repository;

import me.hwanse.chatserver.chatroom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
