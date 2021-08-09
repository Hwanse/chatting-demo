package me.hwanse.chatserver.user.repository;

import me.hwanse.chatserver.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUserId(String userId);

    Optional<User> findUserByUserId(String userId);

}
