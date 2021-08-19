package me.hwanse.chatserver.config;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;

public class ApiDocumentUtil {

    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
            modifyUris().removePort()
        );
    }

}
