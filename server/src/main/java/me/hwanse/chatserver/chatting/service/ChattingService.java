package me.hwanse.chatserver.chatting.service;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.chatting.ChatRoom;
import org.springframework.stereotype.Service;

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

    public Optional<ChatRoom> findChatRoomById(String id) {
        return Optional.ofNullable(rooms.get(id));
    }

    public List<ChatRoom> findAllChatRooms() {
        return new ArrayList<>(rooms.values());
    }

}
