package com.example.study_monster_back.openAi.service;

import com.example.study_monster_back.openAi.dto.response.OpenAiStudyFeedbackResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("openAiService 로 ")
class OpenAiServiceImplTest {
    @Autowired
    private OpenAiServiceImpl openAiServiceImpl;

    @Disabled
    @Test
    @DisplayName("OpenAI로 질의를 할 수 있다")
    void getPlainAnswer() {
        // given
        String systemMessage = "you are a alien. act and answer like alien. do not use english, use your language";
        String userMessage = "hello who are you?";

        // when
        String response = openAiServiceImpl.getPlainAnswer(systemMessage, userMessage);

        // then
        System.out.println(response);
        assertThat(response).isNotEmpty();
    }

    @Disabled
    @Test
    @DisplayName("게시글 피드백을 받을 수 있다")
    void getStudyFeedback() {
        // given
        String title = "Today what I learned";
        String article = """
            오늘은 제가 3주간 진행했던 '피그마 웹 디자인 마스터 클래스'의 마지막 날이었습니다. 이번 과정을 통해 배운 내용과 느낀 점들을 간단히 정리해 보려고 합니다.

            첫 만남, 피그마의 기본기
            처음 피그마를 접했을 때는 솔직히 인터페이스가 복잡해 보여서 조금 겁먹었습니다. 그렇지만 강사님의 친절한 설명 덕분에 기본적인 도구 사용법부터 레이어 관리, 프레임 생성 등 기초적인 기능들을 빠르게 익힐 수 있었습니다. 특히 '오토 레이아웃'과 '컴포넌트' 개념이 혁신적이라고 느꼈어요. 반응형 디자인을 구현할 때 자동으로 레이아웃을 조정해주는 기능은 정말 시간을 절약해주더라고요.
            실전 프로젝트로 배우는 UI 디자인

            두 번째 주차부터는 실제 프로젝트를 진행하면서 디자인 감각을 키우는 시간을 가졌습니다. 개인 기록장 게시판부터 시작해서, 스터디 그룹 모집 페이지까지 다양한 웹 서비스의 UI를 직접 디자인해 보았습니다.
            처음에는 깔끔한 디자인을 위해 무엇을 빼야 할지에 집중했는데, 강사님이 "좋은 디자인은 기능을 돋보이게 하는 것"이라고 말씀해주신 것이 기억에 남습니다. 사용자가 원하는 정보를 쉽게 찾을 수 있도록 시각적 계층 구조를 만드는 연습을 많이 했어요.
             
            프로토타이핑의 힘
            마지막 주차에서는 프로토타이핑에 대해 배웠습니다. 정적인 디자인에 상호작용을 추가하니 마치 실제 웹사이트처럼 작동하는 모습을 보며 큰 성취감을 느꼈습니다.
            특히 상세 페이지의 탭 인터페이스나 모달 창 같은 복잡한 인터랙션도 피그마 내에서 구현할 수 있다는 점이 인상적이었어요. 개발자와 소통할 때도 이렇게 구체적인 프로토타입이 있으면 훨씬 수월할 것 같습니다.
            """;

        // when
        OpenAiStudyFeedbackResponse studyFeedback = openAiServiceImpl.getStudyFeedback(title, article);

        // then
        System.out.println(studyFeedback.getFeedback());
        System.out.println(studyFeedback.getFutureLearningStrategy());
        assertThat(studyFeedback.getFeedback()).isNotEmpty();
        assertThat(studyFeedback.getFutureLearningStrategy()).isNotEmpty();
    }
}