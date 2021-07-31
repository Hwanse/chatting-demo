package me.hwanse.chatserver.chat.voice;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SdpMessage {

    private Long roomId;
    private String fromId;
    private String toId;
    private RTCSessionDescription sdp;
    private String type;

}
