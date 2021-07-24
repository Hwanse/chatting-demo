package me.hwanse.chatserver.chatroom.service;

import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.ChatVisitor;
import me.hwanse.chatserver.chatroom.repository.ChatRoomRepository;
import me.hwanse.chatserver.chatroom.repository.ChatVisitorRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class ChatVisitorServiceTest {

    @InjectMocks
    private ChatVisitorService chatVisitorService;

    @Mock
    private ChatVisitorRepository chatVisitorRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("유저가 특정 채팅방에 접속한다")
    public void addChatVisitorTest() throws Exception {
        // given
        String title = "채팅방1";
        String sessionId = "QxzcEDsa";
        ChatRoom chatRoom = new ChatRoom(title);
        ChatVisitor chatVisitor = new ChatVisitor(chatRoom, sessionId);

        given(chatRoomRepository.findById(any())).willReturn(Optional.of(chatRoom));
        given(chatVisitorRepository.save(chatVisitor)).willReturn(savedChatVisitor(chatVisitor));

        // when
        ChatVisitor saved = chatVisitorService.addChatVisitor(chatRoom.getId(), sessionId);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getChatRoom()).isEqualTo(chatRoom);
        assertThat(saved.getSessionId()).isEqualTo(chatVisitor.getSessionId());
    }

    @Test
    @DisplayName("유저가 특정 채팅방에서 떠난다")
    public void leaveChatVisitor() throws Exception {
        // given
        String title = "채팅방1";
        String sessionId = "QxzcEDsa";
        int userCount = 5;
        ChatRoom chatRoom = ChatRoom.builder()
                .title(title)
                .userCount(userCount)
                .build();
        ChatVisitor chatVisitor = new ChatVisitor(chatRoom, sessionId);

        given(chatVisitorRepository.findBySessionId(any())).willReturn(chatVisitor);
        willDoNothing().given(chatVisitorRepository).delete(chatVisitor);
        given(chatRoomRepository.findById(any())).willReturn(Optional.of(chatRoom));

        // when
        chatVisitorService.leaveChatVisitor(sessionId);

        // then
        assertThat(chatRoom.getUserCount()).isLessThan(userCount);
    }

    private ChatVisitor savedChatVisitor(ChatVisitor chatVisitor) {
        return chatVisitor.toBuilder()
                .id(1L).build();
    }

}