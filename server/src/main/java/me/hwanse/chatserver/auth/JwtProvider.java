package me.hwanse.chatserver.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.user.service.UserDetailsProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class JwtProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";
    private final String HEADER_TYPE = "JWT";

    private final Key key;
    private final JwtProperty jwtProperty;
    private final UserDetailsProvider userDetailsProvider;

    public JwtProvider(JwtProperty jwtProperty, UserDetailsProvider userDetailsProvider) {
        this.jwtProperty = jwtProperty;
        this.key = settingKey(jwtProperty);
        this.userDetailsProvider = userDetailsProvider;
    }

    public Key settingKey(JwtProperty jwtProperty) {
        byte[] decodedKeyBytes = Decoders.BASE64.decode(jwtProperty.getSecret());
        SecretKey secretKey = Keys.hmacShaKeyFor(decodedKeyBytes);
        return secretKey;
    }

    public String createToken(String userId) {
        Date exp = new Date();
        exp.setTime(exp.getTime() + (jwtProperty.getExpiration() * 1000L));

        return Jwts.builder()
                .setHeader(defaultHeader())
                .setIssuer(jwtProperty.getIssuer())
                .setSubject("userInfo")
                .setClaims(createClaimAttributes(userId))
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims verifyToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("잘못된 JWT 서명입니다.");
            throw e;
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.debug("지원되지 않는 JWT 토큰입니다.");
            throw e;
        } catch (IllegalArgumentException e) {
            log.debug("입력된 JWT 토큰이 잘못되었습니다.");
            throw e;
        }
    }

    public Optional<Authentication> getAuthentication(Claims claims) {
        String userId = claims.get("userId", String.class);

        if (StringUtils.hasText(userId)) {
            UserDetails userDetails = userDetailsProvider.loadUserByUsername(userId);
            return Optional.of(
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities())
            );
        } else {
            return Optional.empty();
        }
    }

    public Map<String, Object> defaultHeader() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", HEADER_TYPE);
        headers.put("alg", SignatureAlgorithm.HS256.name());
        return headers;
    }

    public Map<String, Object> createClaimAttributes(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return claims;
    }

}
