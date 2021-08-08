package me.hwanse.chatserver.auth;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();

        if (isUnauthenticatedUser()) {
            String token = resolveToken(httpServletRequest);
            if (token != null) {
                Claims claims = jwtProvider.verifyToken(token).orElseThrow(IllegalAccessError::new);
                Authentication authentication = jwtProvider.getAuthentication(claims, token).orElseThrow(IllegalArgumentException::new);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("인증 정보를 저장했습니다. '{}' / URI : {}", authentication.getPrincipal(),requestURI);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 요청 URL이 '/api/login', '/api/join'일 경우 필터를 거치지 않는다.
     * true 리턴 시 다음 필터로 skip, false 리턴 시 이 Filter 내부에 정의한 doFilterInternal 를 거친다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String requestURI = request.getRequestURI();

        if (pathMatcher.match("/api/login", requestURI)
            || pathMatcher.match("/api/signup", requestURI)) {
            return true;
        }

        return false;
    }

    private boolean isUnauthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private String resolveToken(HttpServletRequest request) {
        String headerToken = request.getHeader(AUTHORIZATION_HEADER);
        String parameterToken = request.getParameter("token");

        if (StringUtils.hasText(headerToken) && headerToken.startsWith(TOKEN_TYPE)) {
            return headerToken.split(" ")[1];
        } else if (StringUtils.hasText(parameterToken) && parameterToken.startsWith(TOKEN_TYPE)) {
            return parameterToken.split(" ")[1];
        }
        return null;
    }

}
