package me.hwanse.chatserver.chatting.service;

import me.hwanse.chatserver.chatting.ChatRoom;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class ChattingServiceTest {

    private ChattingService chattingService;
    private final String TITLE = "채팅방 제목";

    @BeforeEach
    public void setup() {
        chattingService = new ChattingService();
    }

    @Test
    @DisplayName("채팅방을 개설한다")
    public void createChatRoom() throws Exception {
        // when
        ChatRoom room = chattingService.createChatRoom(TITLE);

        // then
        assertThat(room).isNotNull();
        assertThat(room.getId()).isNotEmpty();
        assertThat(room.getTitle()).isEqualTo(TITLE);
        assertThat(room.getUserCount()).isZero();
        assertThat(room.getSessions()).isEmpty();
    }

    @Test
    @DisplayName("채팅방 리스트를 조회한다")
    public void findAllChatRooms() throws Exception {
        // given
        int count = 10;
        for (int i = 0; i < count; i++) {
            chattingService.createChatRoom(TITLE + (i + 1));
        }

        // when
        List<ChatRoom> chatRooms = chattingService.findAllChatRooms();

        // then
        assertThat(chatRooms).hasSize(count);
    }

    @Test
    @DisplayName("채팅방을 id로 조회한다")
    public void findChatRoomById() throws Exception {
        // given
        ChatRoom room = chattingService.createChatRoom(TITLE);

        // when
        ChatRoom result = chattingService.findChatRoomById(room.getId());

        // then
        assertThat(result).isNotNull();
    }

}