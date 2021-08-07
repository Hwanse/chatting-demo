package me.hwanse.chatserver.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

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

}
