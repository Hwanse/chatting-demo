package me.hwanse.chatserver.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hwanse.chatserver.chatroom.ChatRoom;
import me.hwanse.chatserver.api.dto.chatroom.CreateChatRoomRequest;
import me.hwanse.chatserver.chatroom.service.ChatRoomService;
import me.hwanse.chatserver.config.RestDocsConfig;
import me.hwanse.chatserver.config.WebTestWithSecurityConfig;
import me.hwanse.chatserver.config.WithMockJwtAuthentication;
import me.hwanse.chatserver.document.chatroom.ChatRoomDocumentation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ChatRoomController.class)
@Import({WebTestWithSecurityConfig.class, RestDocsConfig.class})
@AutoConfigureRestDocs
class ChatRoomControllerTest {

    @MockBean
    private ChatRoomService chatRoomService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String HAL_JSON_UTF8 = MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8";
    private final String TITLE = "title";
    private final String USER_ID = "admin";

    @Test
    @DisplayName("채팅방 생성 API")
    @WithMockJwtAuthentication
    public void createApiTest() throws Exception {
        // given
        ChatRoom chatRoom = getChatRoom(1L, TITLE, USER_ID);
        given(chatRoomService.createChatRoom(chatRoom.getTitle(), chatRoom.getLimitUserCount(), chatRoom.getManagerId())).willReturn(chatRoom);

        CreateChatRoomRequest createRequest = new CreateChatRoomRequest();
        createRequest.setTitle(TITLE);
        createRequest.setLimitUserCount(chatRoom.getLimitUserCount());

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/chat-room")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(createRequest)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, HAL_JSON_UTF8))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title").exists())
                .andExpect(jsonPath("$.data.limitUserCount").isNumber())
                .andExpect(jsonPath("$.data.userCount").isNumber())
                .andExpect(jsonPath("$.data.managerId").exists())
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.deletedAt").isEmpty())
                .andExpect(jsonPath("$.data.use").value(true))
                .andExpect(jsonPath("$.data.links").exists())
                .andExpect(jsonPath("$.error").hasJsonPath())
                .andDo(ChatRoomDocumentation.createChatRoomApiDocument());
    }

    @Test
    @DisplayName("채팅방 전체 리스트 조회 API")
    @WithMockJwtAuthentication
    public void getAllChatRoomsApi() throws Exception {
        // given
        List<ChatRoom> chatRooms = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            long id = i + 1;
            chatRooms.add(getChatRoom(id, TITLE + id, USER_ID));
        }

        given(chatRoomService.findAllChatRooms()).willReturn(chatRooms);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/chat-room")
                                            .accept(MediaTypes.HAL_JSON)
                                            .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, HAL_JSON_UTF8))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].id").exists())
                .andExpect(jsonPath("$.data.content[0].title").exists())
                .andExpect(jsonPath("$.data.content[0].limitUserCount").exists())
                .andExpect(jsonPath("$.data.content[0].userCount").exists())
                .andExpect(jsonPath("$.data.content[0].createdAt").exists())
                .andExpect(jsonPath("$.data.content[0].deletedAt").isEmpty())
                .andExpect(jsonPath("$.data.content[0].use").exists())
                .andExpect(jsonPath("$.data.content[0].managerId").exists())
                .andExpect(jsonPath("$.data.content[0].meManager").exists())
                .andExpect(jsonPath("$.data.content[0].links").exists())
                .andExpect(jsonPath("$.data.links").exists())
                .andExpect(jsonPath("$.error").hasJsonPath())
                .andDo(ChatRoomDocumentation.getAllChatRoomsApiDocument());
    }

    @Test
    @DisplayName("채팅방 id로 조회 API")
    @WithMockJwtAuthentication
    public void getChatRoomApi() throws Exception {
        // given
        ChatRoom chatRoom = getChatRoom(1L, TITLE, USER_ID);

        given(chatRoomService.findChatRoomById(any())).willReturn(chatRoom);

        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/chat-room/{id}", chatRoom.getId())
                        .accept(MediaTypes.HAL_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, HAL_JSON_UTF8))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title").exists())
                .andExpect(jsonPath("$.data.limitUserCount").isNumber())
                .andExpect(jsonPath("$.data.userCount").isNumber())
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.deletedAt").isEmpty())
                .andExpect(jsonPath("$.data.use").value(true))
                .andExpect(jsonPath("$.data.managerId").exists())
                .andExpect(jsonPath("$.data.meManager").exists())
                .andExpect(jsonPath("$.data.links").exists())
                .andExpect(jsonPath("$.error").hasJsonPath())
                .andDo(ChatRoomDocumentation.getChatRoomApiDocument());
    }

    @Test
    @DisplayName("특정 채팅방 미사용 처리 API")
    @WithMockJwtAuthentication
    public void disableChatRoomApi() throws Exception {
        // given
        ChatRoom chatRoom = getChatRoom(1L, TITLE, USER_ID);

        willDoNothing().given(chatRoomService).disableChatRoom(chatRoom.getId(), USER_ID);

        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/api/chat-room/{id}", chatRoom.getId())
                        .accept(MediaTypes.HAL_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, HAL_JSON_UTF8))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.links").exists())
                .andExpect(jsonPath("$.error").hasJsonPath())
                .andDo(ChatRoomDocumentation.disableChatRoomApiDocument());
    }

    private ChatRoom getChatRoom(Long id, String title, String managerId) {
        return ChatRoom.builder()
                .id(id)
                .title(title)
                .createdAt(LocalDateTime.now())
                .usable(true)
                .limitUserCount(5)
                .managerId(managerId)
                .build();
    }

}