package me.hwanse.chatserver.chat.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.chatroom.service.ChatVisitorService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSocketDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    private final ChatVisitorService chatVisitorService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        chatVisitorService.leaveChatVisitor(event.getSessionId());
    }

}
