package me.hwanse.chatserver.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
            try {
                Claims claims = jwtProvider.verifyToken(token);
                Authentication authentication = jwtProvider.getAuthentication(claims).orElseThrow(IllegalArgumentException::new);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException e) {
                log.debug("unexpected error occurred during jwt verify : {}", e.getMessage(), e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String headerToken = request.getHeader(AUTHORIZATION_HEADER);
        String queryStringToken = request.getParameter("token");

        if (StringUtils.hasText(headerToken) && headerToken.startsWith(TOKEN_TYPE)) {
            return headerToken.split(" ")[1];
        }
        if (StringUtils.hasText(queryStringToken)) {
            return queryStringToken;
        }
        return "";
    }

}
