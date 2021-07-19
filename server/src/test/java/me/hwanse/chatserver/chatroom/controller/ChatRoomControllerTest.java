package me.hwanse.chatserver.chatroom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @MockBean
    private ChatRoomService chatRoomService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String TITLE = "title";

    @Test
    @DisplayName("채팅방 생성 API")
    public void createApiTest() throws Exception {
        // given
        ChatRoom chatRoom = getChatRoom(1L, TITLE);
        given(chatRoomService.createChatRoom(any())).willReturn(chatRoom);

        Map<String, String> request = new HashMap<>();
        request.put("title", TITLE);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/chat-room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.userCount").isNumber())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.deletedAt").isEmpty())
                .andExpect(jsonPath("$.use").value(true));
    }

    @Test
    @DisplayName("채팅방 전체 리스트 조회 API")
    public void getAllChatRoomsApi() throws Exception {
        // given
        List<ChatRoom> chatRooms = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            long id = i + 1;
            chatRooms.add(getChatRoom(id, TITLE + id));
        }

        given(chatRoomService.findAllChatRooms()).willReturn(chatRooms);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/chat-room"));

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").isMap())
                .andExpect(jsonPath("$.[0].id").exists())
                .andExpect(jsonPath("$.[0].title").exists())
                .andExpect(jsonPath("$.[0].userCount").exists())
                .andExpect(jsonPath("$.[0].createdAt").exists())
                .andExpect(jsonPath("$.[0].deletedAt").isEmpty())
                .andExpect(jsonPath("$.[0].use").exists());
    }

    @Test
    @DisplayName("채팅방 id로 조회")
    public void getChatRoomApi() throws Exception {
        // given
        ChatRoom chatRoom = getChatRoom(1L, TITLE);

        given(chatRoomService.findChatRoomById(any())).willReturn(chatRoom);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/chat-room/{id}", chatRoom.getId()));

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.userCount").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.deletedAt").isEmpty())
                .andExpect(jsonPath("$.use").exists());
    }

    private ChatRoom getChatRoom(Long id, String title) {
        return ChatRoom.builder()
                .id(1L)
                .title(title)
                .createdAt(LocalDateTime.now())
                .isUse(true)
                .build();
    }

}