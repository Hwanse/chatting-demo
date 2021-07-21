package me.hwanse.chatserver.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    private Long roomId;

    private String message;

    private String sender;

    private int userCount;

    private MessageType messageType;

}
