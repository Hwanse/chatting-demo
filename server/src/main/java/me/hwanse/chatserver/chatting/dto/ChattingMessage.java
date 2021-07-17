package me.hwanse.chatserver.chatting.dto;

import lombok.Getter;
import lombok.Setter;
import me.hwanse.chatserver.chatting.MessageType;

@Getter
@Setter
public class ChattingMessage {

    private String roomId;

    private String sender;

    private String message;

    private MessageType messageType;

}
