package me.hwanse.chatserver.chat.text;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private Long roomId;

    private String message;

    private String sender;

    private int userCount;

    private MessageType messageType;

    private String sessionId;

}
