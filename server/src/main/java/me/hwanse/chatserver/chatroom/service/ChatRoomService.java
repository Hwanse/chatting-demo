package me.hwanse.chatserver.chatroom.service;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.repository.ChatRoomRepository;
import me.hwanse.chatserver.exception.NotFoundException;
import me.hwanse.chatserver.exception.NotHaveManagerPrivilege;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(String title, int limitUserCount, String managerId) {
        return chatRoomRepository.save(new ChatRoom(title, limitUserCount, managerId));
    }

    public ChatRoom findChatRoomById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ChatRoom.class, id));
    }

    public List<ChatRoom> findAllChatRooms() {
        return chatRoomRepository.findByUsableTrueOrderByCreatedAtDesc();
    }

    @Transactional
    public void disableChatRoom(Long roomId, String userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new NotFoundException(ChatRoom.class, roomId));
        if (StringUtils.equals(userId, chatRoom.getManagerId())) {
            chatRoom.disable();
        } else {
            throw new NotHaveManagerPrivilege(userId);
        }
    }

}
