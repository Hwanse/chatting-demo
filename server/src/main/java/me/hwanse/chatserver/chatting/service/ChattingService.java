package me.hwanse.chatserver.chatting.service;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatting.ChatRoom;
import me.hwanse.chatserver.chatting.dto.ChattingMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final Map<String, ChatRoom> rooms = new HashMap<>();

    public ChatRoom createChatRoom(String title) {
        String uuid = UUID.randomUUID().toString();
        ChatRoom room = new ChatRoom(uuid, title);
        rooms.put(room.getId(), room);
        return room;
    }

    public ChatRoom findChatRoomById(String id) {
        return Optional.ofNullable(rooms.get(id)).orElseThrow(() -> new IllegalArgumentException("채팅방을 조회할 수 없습니다."));
    }

    public List<ChatRoom> findAllChatRooms() {
        return new ArrayList<>(rooms.values());
    }

}
