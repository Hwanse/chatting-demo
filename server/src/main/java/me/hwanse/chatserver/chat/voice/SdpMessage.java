package me.hwanse.chatserver.chat.voice;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class SdpMessage {

    @NotNull
    @Min(1)
    private Long roomId;

    @NotBlank
    private String fromId;

    @NotBlank
    private String toId;

    private RTCSessionDescription sdp;

    private String type;

}
