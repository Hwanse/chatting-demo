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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    private final String TITLE = "title";
    private final String USER_ID = "admin";

    @Test
    @DisplayName("채팅방을 생성한다")
    public void createChatRoomTest() throws Exception {
        // given
        int limitUserCount = 5;
        ChatRoom chatRoom = new ChatRoom(TITLE, limitUserCount, USER_ID);
        given(chatRoomRepository.save(any())).willReturn(savedChatRoom(chatRoom));

        // when
        ChatRoom saved = chatRoomService.createChatRoom(TITLE, limitUserCount, USER_ID);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(chatRoom.getTitle());
        assertThat(saved.getManagerId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("채팅방 전체 리스트를 조회한다")
    public void findAllChatRoomsTest() throws Exception {
        // given
        List<ChatRoom> chatRooms = new ArrayList<>();
        int count = 10;
        for (int i = 0; i < count; i++) {
            chatRooms.add(new ChatRoom(TITLE + (i + 1), USER_ID));
        }

        given(chatRoomRepository.findByUsableTrueOrderByCreatedAtDesc()).willReturn(chatRooms);

        // when
        List<ChatRoom> findChatRooms = chatRoomService.findAllChatRooms();

        // then
        assertThat(findChatRooms).hasSize(count);
    }

    @Test
    @DisplayName("특정 채팅방을 id로 조회한다")
    public void findChatRoomByIdTest() throws Exception {
        // given
        long roomId = 1L;
        Optional<ChatRoom> chatRoom = Optional.ofNullable(new ChatRoom(TITLE, USER_ID));
        given(chatRoomRepository.findById(roomId)).willReturn(chatRoom);

        // when
        ChatRoom findRoom = chatRoomService.findChatRoomById(roomId);

        // then
        assertThat(findRoom).isNotNull();
        assertThat(findRoom).isEqualTo(chatRoom.get());
    }

    @Test
    @DisplayName("특정 채팅방을 닫는다")
    public void disableChatRoomTest() throws Exception {
        // given
        long roomId = 1L;
        Optional<ChatRoom> chatRoom = Optional.ofNullable(new ChatRoom(TITLE, USER_ID));
        given(chatRoomRepository.findById(roomId)).willReturn(chatRoom);

        // when
        chatRoomService.disableChatRoom(roomId, USER_ID);
        ChatRoom room = chatRoom.get();

        // then
        assertThat(room.isUsable()).isFalse();
        assertThat(room.getDeletedAt()).isNotNull();
    }

    private ChatRoom savedChatRoom(ChatRoom chatRoom) {
        return chatRoom.toBuilder()
                .id(1L).build();
    }

}