package me.hwanse.chatserver.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtProperty {

    private String header;

    private String secret;

    private String issuer;

    private long expiration;

}
