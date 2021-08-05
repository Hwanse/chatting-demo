package me.hwanse.chatserver.chat.text;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MonitoringMessage {

    @NotNull
    @Min(1)
    private Long roomId;

    private int userCount;

}
