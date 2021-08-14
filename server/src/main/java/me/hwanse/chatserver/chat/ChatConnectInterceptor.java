package me.hwanse.chatserver.chat;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatConnectInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
        StompCommand command = stompHeaderAccessor.getCommand();

        if (command == StompCommand.CONNECT) {
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

}
