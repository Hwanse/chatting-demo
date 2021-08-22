package me.hwanse.chatserver.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignInRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

}
