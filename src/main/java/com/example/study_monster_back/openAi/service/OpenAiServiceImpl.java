package com.example.study_monster_back.openAi.service;

import com.example.study_monster_back.openAi.dto.response.OpenAiStudyFeedbackResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.study_monster_back.openAi.service.AiPrompt.STUDY_FEEDBACK_SYSTEM;
import static com.example.study_monster_back.openAi.service.AiPrompt.STUDY_FEEDBACK_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {
    @Value("${ai.openai.key}")
    private String openaiKey;
    @Value("${ai.openai.endpoint.chat-completions}")
    private String OPEN_AI_END_POINT;

    private RestClient getOpenAiClient() {
        return RestClient.create().mutate()
            .baseUrl(OPEN_AI_END_POINT)
            .defaultHeader("Authorization", String.format("Bearer %s", openaiKey))
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    public <T> T callApi(String systemMessage, String userMessage, Class<T> responseType) {
        List<OpenAiMessage> messages = List.of(
            OpenAiMessage.builder().role("system").content(systemMessage).build(),
            OpenAiMessage.builder().role("user").content(userMessage).build()
        );
        boolean wrapperOrString = WRAPPER_OR_STRING_TYPES.contains(responseType);
        OpenAiRequest openAiRequest = OpenAiRequest.builder()
            .messages(messages)
            .response_format(
                wrapperOrString ? null :
                    getArticleFeedbackResponseFormatWithAnnotation(responseType.getSimpleName(), extractProperties(responseType))
            )
            .build();
        OpenAiResponse openAiResponse = getOpenAiClient().post().body(openAiRequest).retrieve()
            .onStatus((status) -> {
                if (status.getStatusCode().isError()) {
                    System.out.println(String.format("Open AI 통신 에러 : %s %s", status.getStatusCode().value(), status.getStatusText()));
                    throw new RuntimeException("Open AI 와의 통신에 실패 했습니다.");
                }
                return false;
            }).body(OpenAiResponse.class);
        if (wrapperOrString) {
            return responseType.cast(openAiResponse.getChoices().get(0).getMessage().getContent());
        }
        try {
            return (new ObjectMapper()).readValue(openAiResponse.getChoices().get(0).getMessage().getContent(), responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("반환 json 형식 오류");
        }
    }

    public String getPlainAnswer(String systemMessage, String userMessage) {
        return callApi(systemMessage, userMessage, String.class);
    }

    public OpenAiStudyFeedbackResponse getStudyFeedback(String title, String article) {
        String systemMessage = STUDY_FEEDBACK_SYSTEM.getPrompt();
        String userMessage = String.format(STUDY_FEEDBACK_USER.getPrompt(), title, article);
        return callApi(systemMessage, userMessage, OpenAiStudyFeedbackResponse.class);
    }

    private static final Set<Class> WRAPPER_OR_STRING_TYPES = Set.of(
        Byte.class, Short.class, Integer.class, Long.class,
        Float.class, Double.class, Character.class, Boolean.class, String.class
    );

    private <T> Map<String, Map<String, String>> extractProperties(Class<T> responseType) {
        Map<String, Map<String, String>> properties = new HashMap<>();
        Field[] fields = responseType.getDeclaredFields();
        for (Field field : fields) {
            OpenAiSchema annotation = field.getAnnotation(OpenAiSchema.class);
            String type;
            if (field.getType().equals(Integer.class)) {
                type = "integer";
            } else if (Number.class.isAssignableFrom(field.getType())) { // 숫자
                type = "number";
            } else if (field.getType().equals(String.class)) {
                type = "string";
            } else if (field.getType().equals(Boolean.class)) {
                type = "boolean";
            } else {
                type = "string";
            }
            properties.put(field.getName(), Map.of(
                "type", type,
                "description", (annotation == null ? field.getName() : annotation.description())
            ));
        }
        return properties;
    }

    private Map getArticleFeedbackResponseFormatWithAnnotation(String name, Map<String, Map<String, String>> properties) {
        return Map.of(
            "type", "json_schema",
            "json_schema", Map.of(
                "name", name,
                "schema", Map.of(
                    "type", "object",
                    "properties", properties,
                    "required", properties.keySet(),
                    "additionalProperties", false
                )
            )
        );
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class OpenAiRequest {
        @Builder.Default
        private String model = "gpt-4o";
        private List<OpenAiMessage> messages;
        private Map response_format;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class OpenAiMessage {
        private String role;
        private String content;
    }

    @Getter
    @NoArgsConstructor
    private static class OpenAiResponse {
        private String id;
        private String object;
        private Long created;
        private String model;
        private List<OpenAiResponseChoice> choices;
    }

    @Getter
    @NoArgsConstructor
    private static class OpenAiResponseChoice {
        private Long index;
        private OpenAiResponseMessage message;
        private String finish_reason;
    }

    @Getter
    @NoArgsConstructor
    private static class OpenAiResponseMessage {
        private String role;
        private String content;
        private String refusal;
    }
}
