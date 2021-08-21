package me.hwanse.chatserver.user.controller;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.api.ApiResult;
import me.hwanse.chatserver.user.User;
import me.hwanse.chatserver.user.dto.SignUpRequest;
import me.hwanse.chatserver.user.dto.UserDto;
import me.hwanse.chatserver.user.service.UserService;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/signup", produces = MediaTypes.HAL_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        User joinUser = userService.userSignUp(signUpRequest.getUserId(), signUpRequest.getPassword());
        EntityModel<UserDto> model = EntityModel.of(new UserDto(joinUser));
        model.add(linkTo(AuthenticateController.class).withRel("authenticate"));
        model.add(Link.of("/docs/index.html#user-signup").withRel("profile"));

        return ResponseEntity
                .ok(Response(model));
    }

}
