package me.hwanse.chatserver.user.controller;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.api.ApiResult;
import me.hwanse.chatserver.auth.AuthTokenResponse;
import me.hwanse.chatserver.user.dto.SignInRequest;
import me.hwanse.chatserver.user.service.AuthenticateService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static me.hwanse.chatserver.api.ApiResult.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/signin", produces = MediaTypes.HAL_JSON_VALUE)
public class AuthenticateController {

    private final AuthenticateService authenticateService;

    @PostMapping
    public ResponseEntity authenticate(@RequestBody @Valid SignInRequest signInRequest) {
        String jwt = authenticateService.signIn(signInRequest.getUserId(), signInRequest.getPassword());
        EntityModel<AuthTokenResponse> model = EntityModel.of(new AuthTokenResponse(jwt));
        model.add(Link.of("/docs/index.html#user-authenticate").withRel("profile"));
        return ResponseEntity
                .ok(Response(model));
    }

}
