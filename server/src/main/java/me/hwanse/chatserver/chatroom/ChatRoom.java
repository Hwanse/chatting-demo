package me.hwanse.chatserver.chatroom;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private String managerId;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @Column(name = "use_flag")
    private boolean usable;

    public ChatRoom(String title, String managerId) {
        this(title, 5, managerId);
    }

    public ChatRoom(String title, int limitUserCount, String managerId) {
        this(null, title, limitUserCount, 0, managerId, LocalDateTime.now(), null, true);
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
        this.usable = false;
    }

}
