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

    private int limitUserCount; // default 5

    private int userCount;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private boolean use;


    public ChatRoom(String title) {
        this(title, 5);
    }

    public ChatRoom(String title, int limitUserCount) {
        this(null, title, limitUserCount, 0, LocalDateTime.now(), null, true);
    }

    public void increaseUserCount() {
        if (userCount < limitUserCount) {
           this.userCount++;
        }
    }

    public void decreaseUserCount() {
        if (userCount <= 0) return;
        this.userCount--;
    }

    public void disable() {
        this.deletedAt = LocalDateTime.now();
        this.use = false;
    }

}
