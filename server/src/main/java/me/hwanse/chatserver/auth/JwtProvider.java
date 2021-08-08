package me.hwanse.chatserver.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private final JwtProperty jwtProperty;
    private final Key key;
    private final String HEADER_TYPE = "JWT";

    public JwtProvider(JwtProperty jwtProperty) {
        this.jwtProperty = jwtProperty;
        this.key = settingKey(jwtProperty);
    }

    public Key settingKey(JwtProperty jwtProperty) {
        byte[] decodedKeyBytes = Decoders.BASE64.decode(jwtProperty.getSecret());
        SecretKey secretKey = Keys.hmacShaKeyFor(decodedKeyBytes);
        return secretKey;
    }

    public String createToken(User user) {
        Date exp = new Date();
        exp.setTime(exp.getTime() + (jwtProperty.getExpiration() * 1000L));

        return Jwts.builder()
                .setHeader(defaultHeader())
                .setIssuer(jwtProperty.getIssuer())
                .setSubject("userInfo")
                .setClaims(setClaims(user))
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Optional<Claims> verifyToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.debug("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.debug("JWT 토큰이 잘못되었습니다.");
        }

        return Optional.ofNullable(claims);
    }

    public Optional<Authentication> getAuthentication(Claims claims, String token) {
        String seq = claims.get("seq", String.class);
        String userId = claims.get("userId", String.class);

        if (validateClaims(seq, userId)) {
            // TODO authentication 정보 추출
            return Optional.ofNullable(null);
        } else {
            return Optional.empty();
        }
    }

    private boolean validateClaims(String seq, String userId) {
        if (StringUtils.hasText(seq) && StringUtils.hasText(userId)) {
            return true;
        }
        return false;
    }

    public Map<String, Object> defaultHeader() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", HEADER_TYPE);
        headers.put("alg", SignatureAlgorithm.HS256.name());
        return headers;
    }

    public Map<String, Object> setClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("seq", String.valueOf(user.getId()));
        claims.put("userId", user.getUserId());
        return claims;
    }

}
