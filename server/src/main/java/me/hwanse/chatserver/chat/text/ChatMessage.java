package me.hwanse.chatserver.chat.text;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessage {

    @NotNull
    @Min(1)
    private Long roomId;

    private String message;

    private String sender;

    private MessageType messageType;

    @NotBlank
    private String sessionId;

}
