package me.hwanse.chatserver.auth;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.exception.JwtClaimsVerifyError;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static me.hwanse.chatserver.auth.JwtProvider.AUTHORIZATION_HEADER;
import static me.hwanse.chatserver.auth.JwtProvider.TOKEN_TYPE;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String token = resolveToken(httpServletRequest);
        if (!token.isEmpty()) {
            Claims claims = jwtProvider.verifyToken(token).orElseThrow(JwtClaimsVerifyError::new);
            Authentication authentication = jwtProvider.getAuthentication(claims).orElseThrow(IllegalArgumentException::new);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 요청 URL이 '/api/login', '/api/signup' 일 경우 필터를 거치지 않는다.
     * true 리턴 시 다음 필터로 skip, false 리턴 시 이 Filter 내부에 정의한 doFilterInternal 를 거친다.
     */
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        AntPathMatcher pathMatcher = new AntPathMatcher();
//        String requestURI = request.getRequestURI();
//
//        if (pathMatcher.match("/api/login", requestURI)
//            || pathMatcher.match("/api/signup", requestURI)) {
//            return true;
//        }
//
//        return false;
//    }

    private String resolveToken(HttpServletRequest request) {
        String headerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(headerToken) && headerToken.startsWith(TOKEN_TYPE)) {
            return headerToken.split(" ")[1];
        }
        return "";
    }

}
