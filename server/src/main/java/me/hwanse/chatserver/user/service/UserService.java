package me.hwanse.chatserver.user.service;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.auth.AuthenticationAccessDenied;
import me.hwanse.chatserver.auth.JwtAuthenticationEntryPoint;
import me.hwanse.chatserver.exception.DuplicateException;
import me.hwanse.chatserver.user.User;
import me.hwanse.chatserver.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User userSignUp(String userId, String password) {
        if (checkDuplicatedUserId(userId)) {
            throw new DuplicateException(User.class, userId);
        }

        User user = new User(userId, passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean checkDuplicatedUserId(String userId) {
        return userRepository.existsUserByUserId(userId);
    }

}
