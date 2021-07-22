package me.hwanse.chatserver.chatroom.service;

import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.repository.ChatRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    private final String TITLE = "title";

    @Test
    @DisplayName("채팅방을 생성한다")
    public void createChatRoomTest() throws Exception {
        // given
        ChatRoom chatRoom = new ChatRoom(TITLE);
        given(chatRoomRepository.save(any())).willReturn(chatRoom);

        // when
        ChatRoom saved = chatRoomService.createChatRoom(TITLE);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved).isEqualTo(chatRoom);
        assertThat(saved.getTitle()).isEqualTo(chatRoom.getTitle());
    }

    @Test
    @DisplayName("채팅방 전체 리스트를 조회한다")
    public void findAllChatRoomsTest() throws Exception {
        // given
        List<ChatRoom> chatRooms = new ArrayList<>();
        int count = 10;
        for (int i = 0; i < count; i++) {
            chatRooms.add(new ChatRoom(TITLE + (i + 1)));
        }

        given(chatRoomRepository.findByUseTrueOrderByCreatedAtDesc()).willReturn(chatRooms);

        // when
        List<ChatRoom> findChatRooms = chatRoomService.findAllChatRooms();

        // then
        assertThat(findChatRooms).hasSize(count);
    }

    @Test
    @DisplayName("특정 채팅방을 id로 조회한다")
    public void findChatRoomByIdTest() throws Exception {
        // given
        Optional<ChatRoom> chatRoom = Optional.ofNullable(new ChatRoom(TITLE));
        given(chatRoomRepository.findById(any())).willReturn(chatRoom);

        // when
        ChatRoom findRoom = chatRoomService.findChatRoomById(chatRoom.get().getId());

        // then
        assertThat(findRoom).isNotNull();
        assertThat(findRoom).isEqualTo(chatRoom.get());
    }

}