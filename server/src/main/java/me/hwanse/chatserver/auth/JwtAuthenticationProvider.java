package me.hwanse.chatserver.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.user.service.UserDetailsProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsProvider userDetailsProvider;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserDetails userDetails = userDetailsProvider.loadUserByUsername(userId);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("user password not matched");
        }

        AuthToken authToken = new AuthToken(userId, password);
        String jwt = jwtProvider.createToken(userId);
        authToken.setDetails(jwt);

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
