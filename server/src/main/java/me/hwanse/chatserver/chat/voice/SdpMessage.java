package me.hwanse.chatserver.chat.voice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SdpMessage {

    private String fromId;

    private RTCSessionDescription sdp;

    private String type;

}
