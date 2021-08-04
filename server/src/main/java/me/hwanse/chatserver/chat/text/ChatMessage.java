package me.hwanse.chatserver.chat.text;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @NotNull
    @Min(1)
    private Long roomId;

    private String message;

    private String sender;

//    private int userCount;

    private MessageType messageType;

    @NotEmpty
    private String sessionId;

}
