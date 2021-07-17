package me.hwanse.chatserver.chatting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.hwanse.chatserver.chatting.ChatRoom;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {

    private String id;

    private String title;

    private int userCount;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.title = chatRoom.getTitle();
        this.userCount = chatRoom.getUserCount();
    }
}
