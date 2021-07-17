package me.hwanse.chatserver.chatting;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "id")
public class ChatRoom {

    private String id;

    private String title;

    private int userCount;

    Set<WebSocketSession> sessions = new HashSet<>();

    public ChatRoom(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public void addUser(WebSocketSession session) {
        this.sessions.add(session);
        this.userCount++;
    }

}
