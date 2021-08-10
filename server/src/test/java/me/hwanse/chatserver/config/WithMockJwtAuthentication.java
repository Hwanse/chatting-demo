package me.hwanse.chatserver.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtSecurityContextFactory.class)
public @interface WithMockJwtAuthentication {

    String userId() default "admin";

    String password() default "admin";

    String role() default "ROLE_USER";

}
