package me.hwanse.chatserver.user.repository;

import me.hwanse.chatserver.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
