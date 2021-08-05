package me.hwanse.chatserver.chat.voice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LeaveMessage {

    @NotNull
    @Min(1)
    private Long roomId;

    @NotEmpty
    private String sessionId;

}
