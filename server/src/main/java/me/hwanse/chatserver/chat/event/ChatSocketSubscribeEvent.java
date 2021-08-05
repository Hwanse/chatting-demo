package me.hwanse.chatserver.chat.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.chatroom.service.ChatVisitorService;
import me.hwanse.chatserver.exception.NotFoundException;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSocketSubscribeEvent implements ApplicationListener<SessionSubscribeEvent> {

    private final ChatVisitorService chatVisitorService;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
        long roomId = getRoomId(header.getDestination());

        if (roomId > 0) {
            chatVisitorService.addChatVisitor(roomId, header.getSessionId());
        }
    }

    private long getRoomId(String destination) {
        Pattern pattern = Pattern.compile(".*/sub/chat-room/([0-9]+).*$");
        Matcher matcher = pattern.matcher(destination);
        if (matcher.matches()) {
            return Long.parseLong(matcher.group(1));
        }
        return -1;
    }
}
