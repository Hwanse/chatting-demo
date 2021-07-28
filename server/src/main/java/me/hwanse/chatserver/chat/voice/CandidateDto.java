package me.hwanse.chatserver.chat.voice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDto {

    private String address;
    private String candidate;
    private String component;
    private String foundation;
    private int port;
    private long priority;
    private String protocol;
    private String relatedAddress;
    private long relatedPort;
    private int sdpMLineIndex;
    private String sdpMid;
    private String tcpType;
    private String type;
    private String usernameFragment;

}
