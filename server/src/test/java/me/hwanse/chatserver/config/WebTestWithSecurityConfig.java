package me.hwanse.chatserver.config;

import me.hwanse.chatserver.auth.AuthenticationAccessDenied;
import me.hwanse.chatserver.auth.JwtAuthenticationEntryPoint;
import me.hwanse.chatserver.auth.JwtProvider;
import me.hwanse.chatserver.user.service.UserDetailsProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class WebTestWithSecurityConfig {

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private AuthenticationAccessDenied authenticationAccessDenied;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private UserDetailsProvider userDetailsProvider;

}
