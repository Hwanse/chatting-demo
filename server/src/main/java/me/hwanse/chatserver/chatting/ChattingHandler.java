package me.hwanse.chatserver.chatting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hwanse.chatserver.chatting.dto.ChattingMessage;
import me.hwanse.chatserver.chatting.service.ChattingService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChattingHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChattingService chattingService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChattingMessage messageDto = objectMapper.readValue(message.getPayload(), ChattingMessage.class);
        sendProcess(session, messageDto);
    }

    /**
     * 메세지 전송 시 전체 과정
     */
    public void sendProcess(WebSocketSession socketSession, ChattingMessage messageDto) {
        // 채팅방 조회
        ChatRoom chatRoom = chattingService.findChatRoomById(messageDto.getRoomId());
        // 방 인원 관리
        controlSessionsOfRoom(chatRoom, messageDto.getMessageType(), socketSession);
        // 전송할 메세지
        TextMessage message = produceMessage(messageDto);
        // 메세지 전송
        sendMessageToChatRoom(chatRoom.sessions, message);
    }

    public void sendMessageToChatRoom(Set<WebSocketSession> sessions, TextMessage message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                log.error("메세지 전송 실패 : {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 해당 방의 입장과 퇴장을 처리한다
     */
    public void controlSessionsOfRoom(ChatRoom chatRoom, MessageType type, WebSocketSession socketSession) {
        switch (type) {
            case ENTER:
                chatRoom.enterUser(socketSession);
                break;
            case LEAVE:
                chatRoom.leaveUser(socketSession);
                break;
        }
    }

    /**
     * 메세지 타입에 따라 전송할 메세지를 분기 처리
     */
    public TextMessage produceMessage(ChattingMessage messageDto) {
        String message = null;

        switch (messageDto.getMessageType()) {
            case ENTER:
                message = String.format("%s 님이 입장하셨습니다.", messageDto.getSender());
                break;
            case TALK:
                message = String.format("%s", messageDto.getMessage());
                break;
            case LEAVE:
                message = String.format("%s 님이 나가셨습니다.", messageDto.getSender());
                break;
        }

        return new TextMessage(message);
    }

}
