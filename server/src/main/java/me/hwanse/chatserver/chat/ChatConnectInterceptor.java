package me.hwanse.chatserver.chat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.auth.JwtProvider;
import me.hwanse.chatserver.chatroom.service.ChatVisitorService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.hwanse.chatserver.auth.JwtProvider.AUTHORIZATION_HEADER;
import static me.hwanse.chatserver.auth.JwtProvider.TOKEN_TYPE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatConnectInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
        StompCommand command = stompHeaderAccessor.getCommand();

        if (checkMessageCommandType(command)) {
            String token = resolveToken(stompHeaderAccessor.getFirstNativeHeader(AUTHORIZATION_HEADER));
            checkVerifyTokenResult(token);
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

    private boolean checkMessageCommandType(StompCommand command) {
        return command == StompCommand.CONNECT || command == StompCommand.SUBSCRIBE || command == StompCommand.SEND;
    }

    private String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_TYPE)) {
            return token.split(" ")[1];
        }
        return "";
    }

    private void checkVerifyTokenResult(String token) {
        try {
            if (!token.isEmpty()) {
                jwtProvider.verifyToken(token);
            }
        } catch (JwtException e) {
            log.debug("unexpected error occurred during jwt verify : {}", e.getMessage(), e);
            throw e;
        }
    }

}
