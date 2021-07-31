package me.hwanse.chatserver.chat.voice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RTCSessionDescription {

    private String type;
    private String sdp;

}
