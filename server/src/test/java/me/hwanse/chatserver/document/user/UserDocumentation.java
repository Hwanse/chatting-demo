package me.hwanse.chatserver.document.user;

import me.hwanse.chatserver.config.ApiDocumentUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class UserDocumentation {

    public static RestDocumentationResultHandler signUpApiDocument() {
        return document("user/signup", ApiDocumentUtil.getDocumentRequest(),
                    requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
                    ),
                    requestFields(
                        fieldWithPath("userId").description("회원가입 - 유저ID"),
                        fieldWithPath("password").description("회원가입 - 비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("success").description("api 요청결과 성공 여부"),
                        fieldWithPath("data").description("api 응답 데이터 본문"),
                        fieldWithPath("data.id").description("회원가입된 유저의 key 값"),
                        fieldWithPath("data.userId").description("회원가입된 유저의 ID"),
                        fieldWithPath("data.createdAt").description("회원가입 날짜"),
                        fieldWithPath("data.updatedAt").description("회원수정 날짜(default - 최초에는 회원 가입 날짜)"),
                        fieldWithPath("data.use").description("회원의 사용 여부"),
                        fieldWithPath("error").optional().type(JsonFieldType.OBJECT).description("api 에러 내용(에러가 없을경우 null)")
                    )
                );
    }

    public static RestDocumentationResultHandler signInApiDocument() {
        return document("user/signin",
                    requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
                    ),
                    requestFields(
                        fieldWithPath("userId").description("로그인 - 유저ID"),
                        fieldWithPath("password").description("로그인 - 비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("success").description("api 요청결과 성공 여부"),
                        fieldWithPath("data").description("api 응답 데이터 본문"),
                        fieldWithPath("data.token").description("인증된 사용자의 Token 값"),
                        fieldWithPath("error").optional().type(JsonFieldType.OBJECT).description("api 에러 내용(에러가 없을경우 null)")
                    )
                );
    }

}
