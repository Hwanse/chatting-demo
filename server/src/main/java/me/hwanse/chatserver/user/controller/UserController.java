package me.hwanse.chatserver.user.controller;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.api.ApiResult;
import me.hwanse.chatserver.user.User;
import me.hwanse.chatserver.user.dto.SignUpRequest;
import me.hwanse.chatserver.user.dto.UserDto;
import me.hwanse.chatserver.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static me.hwanse.chatserver.api.ApiResult.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResult signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        User joinUser = userService.userSignUp(signUpRequest.getUserId(), signUpRequest.getPassword());
        return OK(
            new UserDto(joinUser)
        );
    }

}
