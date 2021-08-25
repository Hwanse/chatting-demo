package me.hwanse.chatserver.user.service;

import me.hwanse.chatserver.exception.DuplicateException;
import me.hwanse.chatserver.user.User;
import me.hwanse.chatserver.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("신규 유저가 회원가입을 한다")
    public void userSignUpTest() throws Exception {
        // given
        String userId = "admin";
        String password = "1234";
        User user = new User(userId, passwordEncoder.encode(password));

        given(userRepository.existsUserByUserId(userId)).willReturn(false);
        given(userRepository.save(any())).willReturn(savedUser(user));

        // when
        User saved = userService.userSignUp(userId, password);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getPassword()).isNotEqualTo(password);
        assertThat(passwordEncoder.matches(password, saved.getPassword())).isTrue();
        assertThat(saved.isUsable()).isTrue();
    }

    @Test
    @DisplayName("신규 유저가 입력한 id 값이 중복된 userId일 경우")
    public void duplicatedUserIdTest() throws Exception {
        // given
        String userId = "admin";
        String password = "1234";

        given(userRepository.existsUserByUserId(userId)).willReturn(true);

        // when & then
        assertThrows(DuplicateException.class, () -> userService.userSignUp(userId, password));
    }

    private User savedUser(User user) {
        return user.toBuilder()
                .id(1L)
                .build();
    }

}