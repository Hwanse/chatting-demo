package me.hwanse.chatserver.chat;

import me.hwanse.chatserver.chat.text.ChatMessage;
import me.hwanse.chatserver.chat.text.MessageType;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatStompTest {

    private final String LOCAL_URL_PREFIX = "ws://localhost";

    private final String CHAT_JOIN_ENDPOINT = "/pub/chat/join";
    private final String CHAT_TEXT_TALK_ENDPOINT = "/pub/chat/text/message";
    private final String SUBSCRIBE_CHATROOM_URL = "/sub/chat-room/%d";

    private String WEBSOCKET_FULL_URL;

    @Autowired
    private ChatRoomService chatRoomService;

    @Value("${local.server.port}")
    private int port;

    private CompletableFuture<ChatMessage> completableFuture;

    @BeforeEach
    public void setup() {
        WEBSOCKET_FULL_URL = String.format("%s:%d/ws/chat", LOCAL_URL_PREFIX, port);
        completableFuture = new CompletableFuture<>();
        chatRoomService.createChatRoom("채팅방");
    }

    @Test
    @DisplayName("채팅방 유저 접속 알림 테스트")
    public void userJoinInChatTest() throws Exception {
        // given
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(WEBSOCKET_FULL_URL, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        long roomId = 1L;
        ChatMessage chatMessage = new ChatMessage(roomId, "join message", "user",
                MessageType.JOIN, stompSession.getSessionId());

        // when
        stompSession.subscribe(getSubscribeChatRoomUrl(roomId), new ChatMessageStompFrameHandler());
        stompSession.send(CHAT_JOIN_ENDPOINT, chatMessage);
        ChatMessage returnMessage = completableFuture.get(5, TimeUnit.SECONDS);

        // then
        assertThat(returnMessage).isNotNull();
        assertThat(returnMessage.getRoomId()).isEqualTo(roomId);
        assertThat(returnMessage.getMessageType()).isEqualTo(MessageType.JOIN);
        assertThat(returnMessage.getSessionId()).isEqualTo(stompSession.getSessionId());
    }

    @Test
    @DisplayName("채팅방 텍스트 채팅 전파 테스트")
    public void sendMessageTest() throws Exception {
        // given
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(WEBSOCKET_FULL_URL, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        long roomId = 1L;
        String sender = "user";
        ChatMessage chatMessage = new ChatMessage(roomId, "채팅 내용", sender,
                MessageType.TALK, stompSession.getSessionId());

        // when
        stompSession.subscribe(getSubscribeChatRoomUrl(roomId), new ChatMessageStompFrameHandler());
        stompSession.send(CHAT_TEXT_TALK_ENDPOINT, chatMessage);
        ChatMessage returnMessage = completableFuture.get(5, TimeUnit.SECONDS);

        // then
        assertThat(returnMessage).isNotNull();
        assertThat(returnMessage.getSessionId()).isEqualTo(stompSession.getSessionId());
        assertThat(returnMessage.getSender()).isEqualTo(sender);
        assertThat(returnMessage.getMessageType()).isEqualTo(MessageType.TALK);
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>();
        // 기본 Websocket Java API 를 Sockjs Client 로 사용한다
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private String getSubscribeChatRoomUrl(long roomId) {
        return String.format(SUBSCRIBE_CHATROOM_URL, roomId);
    }

    private class ChatMessageStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return ChatMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            completableFuture.complete((ChatMessage) payload);
        }
    }

}