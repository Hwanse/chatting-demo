package me.hwanse.chatserver.chatroom.service;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(String title) {
        return chatRoomRepository.save(new ChatRoom(title));
    }

    public ChatRoom findChatRoomById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
    }

    public List<ChatRoom> findAllChatRooms() {
        return chatRoomRepository.findByUseTrueOrderByCreatedAtDesc();
    }

    @Transactional
    public void increaseChatRoomUserCount(Long roomId) {
        chatRoomRepository.findById(roomId).ifPresent(ChatRoom::increaseUserCount);
    }

    @Transactional
    public void decreaseChatRoomUserCount(Long roomId) {
        chatRoomRepository.findById(roomId).ifPresent(ChatRoom::decreaseUserCount);
    }

}
