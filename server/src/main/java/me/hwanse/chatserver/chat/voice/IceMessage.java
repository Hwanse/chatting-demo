package me.hwanse.chatserver.chat.voice;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IceMessage {

    private String fromId;
    private String toId;
    private String type;
    private CandidateDto candidate;

}
