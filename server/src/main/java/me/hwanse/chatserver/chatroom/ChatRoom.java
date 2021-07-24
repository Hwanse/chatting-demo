package me.hwanse.chatserver.chatroom;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int userCount;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private boolean use;

    public ChatRoom(String title) {
        this.title = title;
        this.createdAt = LocalDateTime.now();
        this.use = true;
    }

    public void increaseUserCount() {
        this.userCount++;
    }

    public void decreaseUserCount() {
        if (userCount <= 0) return;
        this.userCount--;
    }

}
