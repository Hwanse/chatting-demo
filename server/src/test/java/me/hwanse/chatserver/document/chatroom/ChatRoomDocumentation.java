package me.hwanse.chatserver.document.chatroom;

import me.hwanse.chatserver.config.ApiDocumentUtil;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class ChatRoomDocumentation {

    public static RestDocumentationResultHandler createChatRoomApiDocument() {
        return document("chatroom/create", ApiDocumentUtil.getDocumentRequest(),
                    requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                        headerWithName(HttpHeaders.ACCEPT).description(MediaTypes.HAL_JSON_VALUE)
                    ),
                    requestFields(
                        fieldWithPath("title").description("생성할 채팅방 제목"),
                        fieldWithPath("limitUserCount").description("생성할 채팅방의 입장제한 인원수")
                    ),
                    responseFields(
                        fieldWithPath("success").description("api 요청결과 성공 여부"),
                        fieldWithPath("data").description("api 응답 데이터 본문"),
                        fieldWithPath("data.id").description("채팅방 데이터의 key값"),
                        fieldWithPath("data.title").description("채팅방의 제목"),
                        fieldWithPath("data.limitUserCount").description("채팅방의 입장제한 인원수"),
                        fieldWithPath("data.userCount").description("채팅방의 현재 입장한 인원수"),
                        fieldWithPath("data.createdAt").description("채팅방 생성 날짜"),
                        fieldWithPath("data.deletedAt").optional().type(JsonFieldType.STRING).description("채팅방 비활성화(삭제) 날짜"),
                        fieldWithPath("data.use").description("채팅방 사용 여부"),
                        fieldWithPath("data.managerId").description("채팅방 관리자인 유저ID"),
                        fieldWithPath("data.meManager").description("채팅방 관리자 여부"),
                        fieldWithPath("data.links").optional().type(JsonFieldType.ARRAY).description("생성된 채팅방과 연관된 행위 또는 profile 링크의 모음"),
                        fieldWithPath("data.links[].*").ignored(),
                        fieldWithPath("error").optional().type(JsonFieldType.OBJECT).description("api 에러 내용(에러가 없을경우 null)")
                    )
                );
    }

    public static RestDocumentationResultHandler getAllChatRoomsApiDocument() {
        return document("chatroom/list",
                    responseFields(
                            fieldWithPath("success").description("api 요청결과 성공 여부"),
                            fieldWithPath("data").description("api 응답 데이터 본문"),
                            fieldWithPath("data.links").description("채팅방 리스트 조회 api profile 링크"),
                            fieldWithPath("data.links[].*").ignored(),
                            fieldWithPath("data.content").description("조회된 채팅방 리스트"),
                            fieldWithPath("data.content[].id").description("채팅방 데이터의 key값"),
                            fieldWithPath("data.content[].title").description("채팅방의 제목"),
                            fieldWithPath("data.content[].limitUserCount").description("채팅방의 입장제한 인원수"),
                            fieldWithPath("data.content[].userCount").description("채팅방의 현재 입장한 인원수"),
                            fieldWithPath("data.content[].createdAt").description("채팅방 생성 날짜"),
                            fieldWithPath("data.content[].deletedAt").optional().type(JsonFieldType.STRING).description("채팅방 비활성화(삭제) 날짜"),
                            fieldWithPath("data.content[].use").description("채팅방 사용 여부"),
                            fieldWithPath("data.content[].managerId").description("채팅방 관리자인 유저ID"),
                            fieldWithPath("data.content[].meManager").description("현재 사용자가 해당 채팅방에 대한 Manager 여부"),
                            fieldWithPath("data.content[].links").description("채팅방 항목의 정보를 조회하는 link"),
                            fieldWithPath("data.content[].links[].*").ignored(),
                            fieldWithPath("error").optional().type(JsonFieldType.OBJECT).description("api 에러 내용(에러가 없을경우 null)")
                    )
                );
    }

    public static RestDocumentationResultHandler getChatRoomApiDocument() {
        return document("chatroom/info",
                    pathParameters(
                        parameterWithName("id").description("조회할 채팅방의 key 값")
                    ),
                    responseFields(
                            fieldWithPath("success").description("api 요청결과 성공 여부"),
                            fieldWithPath("data").description("api 응답 데이터 본문"),
                            fieldWithPath("data.id").description("채팅방 데이터의 key값"),
                            fieldWithPath("data.title").description("채팅방의 제목"),
                            fieldWithPath("data.limitUserCount").description("채팅방의 입장제한 인원수"),
                            fieldWithPath("data.userCount").description("채팅방의 현재 입장한 인원수"),
                            fieldWithPath("data.createdAt").description("채팅방 생성 날짜"),
                            fieldWithPath("data.deletedAt").optional().type(JsonFieldType.STRING).description("채팅방 비활성화(삭제) 날짜"),
                            fieldWithPath("data.use").description("채팅방 사용 여부"),
                            fieldWithPath("data.managerId").description("채팅방 관리자인 유저ID"),
                            fieldWithPath("data.meManager").description("현재 사용자가 해당 채팅방에 대한 Manager 여부"),
                            fieldWithPath("data.links").optional().type(JsonFieldType.ARRAY).description("채팅방과 연관된 행위 또는 profile 링크의 모음"),
                            fieldWithPath("data.links[].*").ignored(),
                            fieldWithPath("error").optional().type(JsonFieldType.OBJECT).description("api 에러 내용(에러가 없을경우 null)")
                    )
                );
    }

    public static RestDocumentationResultHandler disableChatRoomApiDocument() {
        return document("chatroom/disable",
                    pathParameters(
                            parameterWithName("id").description("비활성화할 채팅방의 key 값")
                    ),
                    responseFields(
                            fieldWithPath("success").description("api 요청결과 성공 여부"),
                            fieldWithPath("data").optional().type(JsonFieldType.OBJECT).description("api 응답 데이터 본문"),
                            fieldWithPath("data.links").description("채팅방 비활성화 api profile 링크"),
                            fieldWithPath("data.links.*").ignored(),
                            fieldWithPath("error").optional().type(JsonFieldType.OBJECT).description("api 에러 내용(에러가 없을경우 null)")
                    )
                );
    }

}
