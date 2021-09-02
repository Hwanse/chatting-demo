package me.hwanse.chatserver.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class ApiDocumentUtil {

    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
            modifyUris()
            .scheme("https")
            .host("34.64.244.142")
            .removePort()
        );
    }

    public static FieldDescriptor[] commonResponseFields() {
        return new FieldDescriptor[] {
            fieldWithPath("success").description("api 요청결과 성공 여부"),
            fieldWithPath("data").description("api 응답 데이터 본문"),
            fieldWithPath("error").optional().type(JsonFieldType.OBJECT).description("api 에러 내용(에러가 없을경우 null)")
        };
    }

    public static List<FieldDescriptor> makeResponseFields(FieldDescriptor... responseFields) {
        return Stream.concat(
            Stream.of(commonResponseFields())
            , Stream.of(responseFields)
        ).collect(Collectors.toList());
    }

    public static List<FieldDescriptor> makeResponseFields(List<FieldDescriptor> responseFields) {
        return Stream.concat(
            Stream.of(commonResponseFields())
            , responseFields.stream()
        ).collect(Collectors.toList());
    }

}
