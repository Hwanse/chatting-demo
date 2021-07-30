package me.hwanse.chatserver.chat.voice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveMessage {

    private Long roomId;
    private String sessionId;

}
