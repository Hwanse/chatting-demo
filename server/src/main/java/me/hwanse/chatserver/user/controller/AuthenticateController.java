package me.hwanse.chatserver.user.controller;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.api.ApiResult;
import me.hwanse.chatserver.auth.AuthTokenResponse;
import me.hwanse.chatserver.user.dto.SignInRequest;
import me.hwanse.chatserver.user.service.AuthenticateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static me.hwanse.chatserver.api.ApiResult.Response;

@RestController
@RequiredArgsConstructor
public class AuthenticateController {

    private final AuthenticateService authenticateService;

    @PostMapping("/api/signin")
    public ApiResult authenticate(@RequestBody @Valid SignInRequest signInRequest) {
        String jwt = authenticateService.signIn(signInRequest.getUserId(), signInRequest.getPassword());
        return Response(
            new AuthTokenResponse(jwt)
        );
    }

}
