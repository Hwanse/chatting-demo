package me.hwanse.chatserver.chat.voice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IceMessage {

    private String fromId;
    private String type;
    private CandidateDto candidate;

}
