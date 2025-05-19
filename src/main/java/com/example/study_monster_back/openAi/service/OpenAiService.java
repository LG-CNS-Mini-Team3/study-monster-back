package com.example.study_monster_back.openAi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static com.example.study_monster_back.openAi.service.AiMessageRole.SYSTEM;
import static com.example.study_monster_back.openAi.service.AiMessageRole.USER;
import static com.example.study_monster_back.openAi.service.AiPrompt.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {
    @Value("${ai.openai.key}")
    private String openaiKey;
    @Value("${ai.openai.endpoint.chat-completions}")
    private String OPEN_AI_END_POINT;
    private final ObjectMapper objectMapper;

    private RestClient getOpenAiClient() {
        return RestClient.create().mutate()
            .baseUrl(OPEN_AI_END_POINT)
            .defaultHeader("Authorization", String.format("Bearer %s", openaiKey))
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    private <T> T callApi(OpenAiRequest openAiRequest, Class<T> responseType) {
        ParameterizedTypeReference<OpenAiResponse<T>> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<OpenAiResponse<T>> responseEntity = getOpenAiClient().post().body(openAiRequest).retrieve()
            .onStatus((status) -> {
                if (status.getStatusCode().isError()) {
                    log.error(String.format("Open AI 통신 에러 : %s %s", status.getStatusCode().value(), status.getStatusText()));
                    throw new RuntimeException("Open AI 와의 통신에 실패 했습니다.");
                }
                return false;
            }).toEntity(typeRef);

        Object content = responseEntity.getBody().getChoices().get(0).getMessage().getContent();
        if (responseType.isInstance(content)) {
            return responseType.cast(content);
        } else if (content instanceof String str) {
            try {
                return objectMapper.readValue(str, responseType);
            } catch (JsonProcessingException e) {
                log.error(String.format("타입 캐스팅 실패 : %s", e.getMessage()));
                throw new RuntimeException("타입 캐스팅 실패");
            }
        } else {
            throw new RuntimeException("타입 캐스팅 실패");
        }
    }

    public String getPlainAnswer(String systemMessage, String userMessage) {
        OpenAiRequest openAiRequest = OpenAiRequest.builder()
            .messages(getPromptedMessages(systemMessage, userMessage))
            .build();
        return callApi(openAiRequest, String.class);
    }

    public StudyFeedbackResponse getStudyFeedback(String title, String article) {
        String systemMessage = STUDY_FEEDBACK_SYSTEM.getPrompt();
        String userMessage = String.format(STUDY_FEEDBACK_USER.getPrompt(), title, article);
        OpenAiRequest<StudyFeedbackProperties> openAiRequest = OpenAiRequest.<StudyFeedbackProperties>builder()
            .messages(getPromptedMessages(systemMessage, userMessage))
            .response_format(getStudyFeedbackResponseFormat())
            .build();
        return callApi(openAiRequest, StudyFeedbackResponse.class);
    }

    private List<OpenAiMessage> getPromptedMessages(String systemMessage, String userMessage) {
        return List.of(
            OpenAiMessage.builder().role(SYSTEM.getRole()).content(systemMessage).build(),
            OpenAiMessage.builder().role(USER.getRole()).content(userMessage).build()
        );
    }

    private OpenAiResponseFormat<StudyFeedbackProperties> getStudyFeedbackResponseFormat() {
        return OpenAiResponseFormat.<StudyFeedbackProperties>builder()
            .json_schema(JsonSchema.<StudyFeedbackProperties>builder()
                .schema(Schema.<StudyFeedbackProperties>builder()
                    .properties(StudyFeedbackProperties.builder()
                        .feedback(PropertyType.builder()
                            .description(STUDY_FEEDBACK_PROPERTY_DESCRIPTION_FEEDBACK.getPrompt())
                            .build())
                        .futureLearningStrategy(PropertyType.builder()
                            .description(STUDY_FEEDBACK_PROPERTY_DESCRIPTION_STRATEGY.getPrompt())
                            .build())
                        .build())
                    .build())
                .build())
            .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class OpenAiRequest<T> {
        @Builder.Default
        private String model = "gpt-4o";
        private List<OpenAiMessage> messages;
        private OpenAiResponseFormat<T> response_format;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class OpenAiMessage {
        private String role;
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class OpenAiResponseFormat<T> {
        @Builder.Default
        private String type = "json_schema";
        private JsonSchema<T> json_schema;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class JsonSchema<T> {
        @Builder.Default
        private String name = "study_feedback";
        private Schema<T> schema;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class Schema<T> {
        @Builder.Default
        private String type = "object";
        private T properties;
        @Builder.Default
        private List<String> required = List.of("feedback", "futureLearningStrategy");
        @Builder.Default
        private Boolean additionalProperties = false;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class StudyFeedbackProperties {
        private PropertyType feedback;
        private PropertyType futureLearningStrategy;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class PropertyType {
        @Builder.Default
        private String type = "string";
        private String description;
    }


    @Getter
    @NoArgsConstructor
    private static class OpenAiResponse<T> {
        private String id;
        private String object;
        private Long created;
        private String model;
        private List<OpenAiResponseChoice<T>> choices;
    }

    @Getter
    @NoArgsConstructor
    private static class OpenAiResponseChoice<T> {
        private Long index;
        private OpenAiResponseMessage<T> message;
        private String finish_reason;
    }

    @Getter
    @NoArgsConstructor
    private static class OpenAiResponseMessage<T> {
        private String role;
        private T content;
        private String refusal;
    }

    @Getter
    @NoArgsConstructor
    public static class StudyFeedbackResponse {
        private String feedback;
        private String futureLearningStrategy;
    }
}
