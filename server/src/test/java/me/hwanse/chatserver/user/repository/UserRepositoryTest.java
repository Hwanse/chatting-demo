package me.hwanse.chatserver.user.repository;

import me.hwanse.chatserver.TestConfig;
import me.hwanse.chatserver.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.save(new User("admin", "admin"));
    }

    @Test
    @DisplayName("입력된 userId를 가진 유저 데이터의 존재 여부 체크")
    public void existsUserByUserIdTest() throws Exception {
        // given
        String existsUserId = "admin";
        String nonExistsUserId = "test";

        // when
        boolean exists = userRepository.existsUserByUserId(existsUserId);
        boolean nonExists = userRepository.existsUserByUserId(nonExistsUserId);

        // then
        assertThat(exists).isTrue();
        assertThat(nonExists).isFalse();
    }

}