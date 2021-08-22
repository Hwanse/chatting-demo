package me.hwanse.chatserver.chatroom.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class CreateChatRoomRequest {

    @NotBlank
    private String title;

    @Min(1)
    @Max(10)
    private int limitUserCount = 5;

}
