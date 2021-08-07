package me.hwanse.chatserver.user.service;

import lombok.RequiredArgsConstructor;
import me.hwanse.chatserver.user.User;
import me.hwanse.chatserver.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Transactional
    public User userSignUp(String userId, String password) {
        // TODO id 중복 체크

        // TODO 유저 회원 가입
        return null;
    }

}
