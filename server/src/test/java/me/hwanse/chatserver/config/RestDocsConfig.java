package me.hwanse.chatserver.config;

import me.hwanse.chatserver.api.dto.ChatRoomDtoToModelConverter;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration
public class RestDocsConfig {

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer.operationPreprocessors()
                                    .withRequestDefaults(prettyPrint())
                                    .withResponseDefaults(prettyPrint());
    }

    @Bean
    public ChatRoomDtoToModelConverter chatRoomDtoToModelConverter() {
        return new ChatRoomDtoToModelConverter();
    }

}
