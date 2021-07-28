package me.hwanse.chatserver.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.hwanse.chatserver.chatroom.ChatVisitor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatVisitorDto {

    private Long id;

    private Long roomId;

    private String sessionId;

    public ChatVisitorDto(ChatVisitor chatVisitor) {
        this.id = chatVisitor.getId();
        this.roomId = chatVisitor.getChatRoom().getId();
        this.sessionId = chatVisitor.getSessionId();
    }

}
