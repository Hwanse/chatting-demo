package me.hwanse.chatserver.chatroom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.chatroom.ChatRoom;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {

    private Long id;

    private String title;

    private int limitUserCount;

    private int userCount;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private boolean use;

    private String managerId;

    private boolean meManager;

    public ChatRoomDto(ChatRoom chatRoom, String userId) {
        this.id = chatRoom.getId();
        this.title = chatRoom.getTitle();
        this.limitUserCount = chatRoom.getLimitUserCount();
        this.userCount = chatRoom.getUserCount();
        this.createdAt = chatRoom.getCreatedAt();
        this.deletedAt = chatRoom.getDeletedAt();
        this.use = chatRoom.isUsable();
        this.managerId = chatRoom.getManagerId();
        this.meManager = StringUtils.equals(chatRoom.getManagerId(), userId);
    }
}
