package me.hwanse.chatserver.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.chatroom.service.ChatVisitorService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatConnectInterceptor implements ChannelInterceptor {

    private final ChatVisitorService chatVisitorService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
        StompCommand command = stompHeaderAccessor.getCommand();

        if (command == StompCommand.SUBSCRIBE) {
            Long roomId = getRoomId(stompHeaderAccessor.getDestination());
            chatVisitorService.addChatVisitor(roomId, stompHeaderAccessor.getSessionId());

        } else if (command == StompCommand.DISCONNECT) {
            chatVisitorService.leaveChatVisitor(stompHeaderAccessor.getSessionId());
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

    private long getRoomId(String destination) {
        Pattern pattern = Pattern.compile(".*/sub/chat-room/([0-9]+)/.*$");
        Matcher matcher = pattern.matcher(destination);
        if (matcher.matches()) {
            return Long.parseLong(matcher.group(1));
        }
        return -1;
    }
}
