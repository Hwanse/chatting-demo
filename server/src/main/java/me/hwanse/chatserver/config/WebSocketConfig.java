package me.hwanse.chatserver.config;

import me.hwanse.chatserver.chatting.ChattingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chattingHandler(), "/ws/chat")
                .setAllowedOrigins("*");
    }

    @Bean
    public ChattingHandler chattingHandler() {
        return new ChattingHandler();
    }
}
