package me.hwanse.chatserver.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.chatting.dto.ChattingMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChattingHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChattingMessage messageDto = objectMapper.readValue(message.getPayload(), ChattingMessage.class);
        TextMessage welcomeMessage = new TextMessage(objectMapper.writeValueAsString(messageDto));
        session.sendMessage(welcomeMessage);
    }

}
