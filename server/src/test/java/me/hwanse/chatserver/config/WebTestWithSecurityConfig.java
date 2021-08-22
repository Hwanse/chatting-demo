package me.hwanse.chatserver.config;

import me.hwanse.chatserver.auth.AuthenticationAccessDenied;
import me.hwanse.chatserver.auth.JwtAuthenticationEntryPoint;
import me.hwanse.chatserver.auth.JwtProvider;
import me.hwanse.chatserver.user.service.UserDetailsProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * MVC 슬라이스 테스트에서 SecurityConfig class 내부에서 하위 필드들에 대한 Bean들이 등록되지 않아
 * 테스트 실행 시 Bean 주입 관련 이슈가 생긴다. 따라서 해당 Bean들에 대한 Mocking 필요
 */
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
