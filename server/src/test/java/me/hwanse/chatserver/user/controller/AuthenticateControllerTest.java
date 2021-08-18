package me.hwanse.chatserver.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import me.hwanse.chatserver.auth.JwtProvider;
import me.hwanse.chatserver.config.RestDocsConfig;
import me.hwanse.chatserver.config.WebTestWithSecurityConfig;
import me.hwanse.chatserver.document.user.UserDocumentation;
import me.hwanse.chatserver.user.dto.SignInRequest;
import me.hwanse.chatserver.user.service.AuthenticateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = AuthenticateController.class)
@Import({WebTestWithSecurityConfig.class, RestDocsConfig.class})
@ExtendWith(RestDocumentationExtension.class)
class AuthenticateControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticateService authenticateService;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("유저 인증 토큰 발급 API")
    public void signInTest() throws Exception {
        // given
        String userId = "admin";
        String password = "1234";
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUserId(userId);
        signInRequest.setPassword(password);

        String token = createToken(userId);
        given(authenticateService.signIn(userId, password)).willReturn(token);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/signin")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(signInRequest)));

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.token").value(token))
                .andExpect(jsonPath("$.error").hasJsonPath())
                .andDo(UserDocumentation.signInApiDocument());
    }

    private String createToken(String userId) {
        byte[] decodedKeyBytes = Decoders.BASE64.decode("aHdhbnNlLXNwcmluZy1zZWN1cml0eS1qd3QtYXV0aC1zZXJ2ZXItc2VjcmV0LWtleQo");
        Key key = Keys.hmacShaKeyFor(decodedKeyBytes);

        Date exp = new Date();
        exp.setTime(exp.getTime() + (3600 * 1000L));

        return Jwts.builder()
                .setHeader(defaultHeader())
                .setIssuer("chat-server")
                .setSubject("userInfo")
                .setClaims(createClaimAttributes(userId))
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> defaultHeader() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", SignatureAlgorithm.HS256.name());
        return headers;
    }

    private Map<String, Object> createClaimAttributes(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return claims;
    }
}