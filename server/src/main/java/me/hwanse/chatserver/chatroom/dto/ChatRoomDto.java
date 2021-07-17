package me.hwanse.chatserver.chatroom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.hwanse.chatserver.chatroom.ChatRoom;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {

    private Long id;

    private String title;

    private int userCount;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private boolean use;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.title = chatRoom.getTitle();
        this.userCount = chatRoom.getUserCount();
        this.createdAt = chatRoom.getCreatedAt();
        this.deletedAt = chatRoom.getDeletedAt();
        this.use = chatRoom.isUse();
    }
}
