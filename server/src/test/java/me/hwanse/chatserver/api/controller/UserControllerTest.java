package me.hwanse.chatserver.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hwanse.chatserver.config.RestDocsConfig;
import me.hwanse.chatserver.config.WebTestWithSecurityConfig;
import me.hwanse.chatserver.document.user.UserDocumentation;
import me.hwanse.chatserver.user.User;
import me.hwanse.chatserver.api.dto.user.SignUpRequest;
import me.hwanse.chatserver.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class)
@Import({WebTestWithSecurityConfig.class, RestDocsConfig.class})
@AutoConfigureRestDocs
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final String HAL_JSON_UTF8 = MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8";

    @Test
    @DisplayName("회원 가입 API")
    public void signUpAPI() throws Exception {
        // given
        String userId = "admin";
        String password = "1234";
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserId(userId);
        signUpRequest.setPassword(password);

        given(userService.userSignUp(userId, password)).willReturn(joinedUser(userId));

        // when
        ResultActions resultActions =
                mockMvc.perform(post("/api/signup")
                        .accept(MediaTypes.HAL_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, HAL_JSON_UTF8))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.updatedAt").exists())
                .andExpect(jsonPath("$.data.use").value(true))
                .andExpect(jsonPath("$.data.links").exists())
                .andExpect(jsonPath("$.error").hasJsonPath())
                .andDo(UserDocumentation.signUpApiDocument())
        ;
    }

    private User joinedUser(String userId) {
        return User.builder()
                .id(1L)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .usable(true)
                .build();
    }

}