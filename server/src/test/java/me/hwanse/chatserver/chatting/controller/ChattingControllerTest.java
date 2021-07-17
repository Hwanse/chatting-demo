package me.hwanse.chatserver.chatting.controller;

import me.hwanse.chatserver.chatting.ChatRoom;
import me.hwanse.chatserver.chatting.service.ChattingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ChattingController.class)
@ExtendWith(MockitoExtension.class)
class ChattingControllerTest {

    @MockBean
    private ChattingService chattingService;

    @Autowired
    private MockMvc mockMvc;

    private final String TITLE = "채팅방 제목";

    private ChatRoom newchatRoom(String title) {
        return new ChatRoom(UUID.randomUUID().toString(), title);
    }

    @Test
    @DisplayName("채팅방 생성 API")
    public void createChatRoomApi() throws Exception {
        // given
        ChatRoom room = newchatRoom(TITLE);
        given(chattingService.createChatRoom(any())).willReturn(room);

        // when
        ResultActions resultActions = mockMvc.perform(post("/chattings").content(TITLE));

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.userCount").exists());
    }

    @Test
    @DisplayName("채팅방 리스트 조회")
    public void chatRoomsApi() throws Exception {
        // given
        List<ChatRoom> chatRooms = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chatRooms.add(newchatRoom(TITLE + (i + 1)));
        }
        given(chattingService.findAllChatRooms()).willReturn(chatRooms);

        // when
        ResultActions resultActions = mockMvc.perform(get("/chattings"));

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isMap())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].userCount").exists());
    }

}