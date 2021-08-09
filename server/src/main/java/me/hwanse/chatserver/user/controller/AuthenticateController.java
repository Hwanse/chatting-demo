package me.hwanse.chatserver.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.api.ApiResult;
import me.hwanse.chatserver.auth.AuthTokenResponse;
import me.hwanse.chatserver.user.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static me.hwanse.chatserver.api.ApiResult.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticateController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/api/login")
    public ApiResult login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword())
        );

        log.info("authentication controller");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = (String) authentication.getDetails();
        return OK(
            new AuthTokenResponse(jwt)
        );
    }

}
