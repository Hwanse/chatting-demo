package me.hwanse.chatserver.user.service;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserDetailsProvider implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findUserByUserId(userId)
                .map(user -> new org.springframework.security.core.userdetails.User(userId, user.getPassword(), getAuthorities()))
                .orElseThrow(() -> new UsernameNotFoundException(userId + " is not found user."));
    }

    private Set<SimpleGrantedAuthority> getAuthorities() {
        return Stream.of(new SimpleGrantedAuthority("ROLE_USER")).collect(Collectors.toSet());
    }

}
