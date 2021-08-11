package me.hwanse.chatserver.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hwanse.chatserver.auth.AuthenticationAccessDenied;
import me.hwanse.chatserver.auth.JwtAuthenticationEntryPoint;
import me.hwanse.chatserver.auth.JwtProvider;
import me.hwanse.chatserver.config.WebTestWithSecurityConfig;
import me.hwanse.chatserver.user.User;
import me.hwanse.chatserver.user.dto.SignUpRequest;
import me.hwanse.chatserver.user.service.UserDetailsProvider;
import me.hwanse.chatserver.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = UserController.class)
@Import(WebTestWithSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("회원 가입 API")
    public void signUpAPI() throws Exception {
        // given
        String userId = "admin";
        String password = "1234";
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserId(userId);
        signUpRequest.setPassword(password);

        given(userService.userSignUp(userId, password)).willReturn(joinedUser(userId, password));

        // when
        ResultActions resultActions =
                mockMvc.perform(post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequest)));

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.updatedAt").exists())
                .andExpect(jsonPath("$.data.use").value(true))
                .andExpect(jsonPath("$.error").isEmpty());
    }

    private User joinedUser(String userId, String password) {
        return User.builder()
                .id(1L)
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .use(true)
                .build();
    }

}