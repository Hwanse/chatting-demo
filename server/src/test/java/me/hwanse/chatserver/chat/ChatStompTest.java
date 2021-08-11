package me.hwanse.chatserver.chat;

import me.hwanse.chatserver.auth.JwtProvider;
import me.hwanse.chatserver.chat.text.ChatMessage;
import me.hwanse.chatserver.chat.text.MessageType;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import me.hwanse.chatserver.config.WithMockJwtAuthentication;
import me.hwanse.chatserver.exception.NotFoundException;
import me.hwanse.chatserver.user.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
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

    private final String USER_ID = "admin";

    private String WEBSOCKET_FULL_URL;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private UserService userService;

    @Value("${local.server.port}")
    private int port;

    private CompletableFuture<ChatMessage> completableFuture;

    @Autowired
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setup() {
        WEBSOCKET_FULL_URL = String.format("%s:%d/ws/chat", LOCAL_URL_PREFIX, port);
        completableFuture = new CompletableFuture<>();
        chatRoomService.createChatRoom("채팅방", 5);
        userService.userSignUp(USER_ID, USER_ID);
        for (int i = 0; i < 5; i++) {
            userService.userSignUp("user" + (i + 1), "user" + (i + 1));
        }
    }

    @Test
    @DisplayName("채팅방 유저 접속 알림 테스트")
    public void userJoinInChatTest() throws Exception {
        // given
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        webSocketHttpHeaders.add(JwtProvider.AUTHORIZATION_HEADER, JwtProvider.TOKEN_TYPE + " " + jwtProvider.createToken(USER_ID));

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add(JwtProvider.AUTHORIZATION_HEADER, jwtProvider.createToken(USER_ID));
        StompSession stompSession = stompClient.connect(WEBSOCKET_FULL_URL, webSocketHttpHeaders, stompHeaders, getStompSessionHandlerAdapter())
                .get(1, TimeUnit.SECONDS);

        long roomId = 1L;
        ChatMessage chatMessage = new ChatMessage(roomId, "join message", "user",
                MessageType.JOIN, stompSession.getSessionId());

        // when
        stompSession.subscribe(getSubscribeChatRoomUrl(roomId), new ChatMessageStompFrameHandler(completableFuture));
        stompSession.send(CHAT_JOIN_ENDPOINT, chatMessage);

        ChatMessage returnMessage = completableFuture.join();

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
        /* 메시지 수신자들 */
        long roomId = 1L;
        String sender = "user";

        List<StompSession> sessionList = new ArrayList<>();
        List<CompletableFuture<ChatMessage>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            WebSocketStompClient tempStompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
            tempStompClient.setMessageConverter(new MappingJackson2MessageConverter());
            CompletableFuture<ChatMessage> future = new CompletableFuture<>();

            WebSocketHttpHeaders tempWebSocketHttpHeaders = new WebSocketHttpHeaders();
            String token = jwtProvider.createToken("user" + (i + 1));
            tempWebSocketHttpHeaders.add(JwtProvider.AUTHORIZATION_HEADER, JwtProvider.TOKEN_TYPE + " " + token);

            StompHeaders tempStompHeaders = new StompHeaders();
            tempStompHeaders.add(JwtProvider.AUTHORIZATION_HEADER, token);

            StompSession session = tempStompClient.connect(WEBSOCKET_FULL_URL, tempWebSocketHttpHeaders, tempStompHeaders,getStompSessionHandlerAdapter()).get(1, TimeUnit.SECONDS);
            session.subscribe(getSubscribeChatRoomUrl(roomId), new ChatMessageStompFrameHandler(future));

            futures.add(future);
            sessionList.add(session);
        }

        /* 메시지 전송자 */
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        webSocketHttpHeaders.add(JwtProvider.AUTHORIZATION_HEADER, JwtProvider.TOKEN_TYPE + " " + jwtProvider.createToken(USER_ID));

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add(JwtProvider.AUTHORIZATION_HEADER, jwtProvider.createToken(USER_ID));

        StompSession senderSession = stompClient.connect(WEBSOCKET_FULL_URL, webSocketHttpHeaders, stompHeaders, getStompSessionHandlerAdapter())
                .get(1, TimeUnit.SECONDS);

        ChatMessage chatMessage = new ChatMessage(roomId, "채팅 내용", sender, MessageType.TALK, senderSession.getSessionId());

        // when
        senderSession.subscribe(getSubscribeChatRoomUrl(roomId), new ChatMessageStompFrameHandler(completableFuture));
        senderSession.send(CHAT_TEXT_TALK_ENDPOINT, chatMessage);
        ChatMessage returnMessage = completableFuture.join();

        // then
        assertThat(returnMessage).isNotNull();
        assertThat(returnMessage.getSessionId()).isEqualTo(senderSession.getSessionId());
        assertThat(returnMessage.getSender()).isEqualTo(sender);
        assertThat(returnMessage.getMessageType()).isEqualTo(MessageType.TALK);

        assertThat(futures).allSatisfy(future -> {
            ChatMessage message = future.join();
            assertThat(message).isNotNull();
            assertThat(message.getSender()).isEqualTo(sender);
            assertThat(message.getSessionId()).isEqualTo(senderSession.getSessionId());
        });
    }

    private StompSessionHandlerAdapter getStompSessionHandlerAdapter() {
        return new StompSessionHandlerAdapter() {};
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

    static class ChatMessageStompFrameHandler implements StompFrameHandler {

        private final CompletableFuture<ChatMessage> completableFuture;

        public ChatMessageStompFrameHandler(CompletableFuture<ChatMessage> completableFuture) {
            this.completableFuture = completableFuture;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return ChatMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.completableFuture.complete((ChatMessage) payload);
        }

    }

}
