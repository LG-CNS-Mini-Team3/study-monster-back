package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.dto.response.StudyFeedbackResponse;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.feedback.entity.Feedback;
import com.example.study_monster_back.feedback.respository.FeedbackRepository;
import com.example.study_monster_back.openAi.dto.response.OpenAiStudyFeedbackResponse;
import com.example.study_monster_back.openAi.service.OpenAiService;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DisplayName("board 서비스로 ")
class BoardServiceImplTest {
    @Autowired
    private BoardServiceImpl boardServiceImpl;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @MockitoSpyBean
    private OpenAiService openAiService;

    @AfterEach
    void tearDown() {
        feedbackRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("게시글의 정보를 가져올 수 있다.")
    void getBoard() {
        // given
        User user = new User();
        user.setEmail("email");
        user.setName("name");
        user.setPwd("pwd");
        user.setRole("role");
        user.setNickname("nickName");
        user.setPhone_number("010-0000-0000");

        userRepository.save(user);
        Board board = new Board();
        board.setUser(user);
        board.setTitle("title");
        board.setContent("content");
        boardRepository.save(board);

        Board board2 = new Board();
        board2.setUser(user);
        board2.setTitle("title2");
        board2.setContent("content2");
        boardRepository.save(board2);
        System.out.println(board.getId());
        System.out.println(board2.getId());

        // when
        GetBoardResponseDto response = boardServiceImpl.getBoard(board.getId());

        // then
        assertThat(response)
            .extracting("boardId", "title", "content", "userId", "email", "nickname")
            .containsExactly(board.getId(), board.getTitle(), board.getContent(), user.getId(), user.getEmail(), user.getNickname());
    }

    @Test
    @DisplayName("게시글의 정보가 없을 경우 예외가 발생한다.")
    void getBoardWhenNoData() {
        // given
        User user = new User();
        user.setEmail("email");
        user.setName("name");
        user.setPwd("pwd");
        user.setRole("role");
        user.setNickname("nickName");
        user.setPhone_number("010-0000-0000");
        userRepository.save(user);

        // when
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
            () -> boardServiceImpl.getBoard(1L));

        // then
        assertThat(runtimeException.getMessage())
            .isEqualTo("해당 id를 가진 게시글이 없습니다.");
    }

    @Test
    @DisplayName("피드백이 존재하지 않는 게시글일 경우 새로운 피드백 데이터가 반환된다.")
    void getStudyFeedback() {
        // given
        User user = new User();
        user.setEmail("email");
        user.setName("name");
        user.setPwd("pwd");
        user.setRole("role");
        user.setNickname("nickName");
        user.setPhone_number("010-0000-0000");
        userRepository.save(user);

        Board board = boardRepository.save(Board.builder()
            .user(user)
            .title("title")
            .content("content")
            .build());

        OpenAiStudyFeedbackResponse mockResponse = new OpenAiStudyFeedbackResponse();
        Mockito.doReturn(mockResponse).when(openAiService).getStudyFeedback(anyString(), anyString());

        // when
        StudyFeedbackResponse response = boardServiceImpl.getStudyFeedback(board.getId());

        // then
        assertThat(response.getFeedback()).isNull();
        assertThat(response.getFutureLearningStrategy()).isNull();
        verify(openAiService, times(1)).getStudyFeedback(anyString(), anyString());
    }

    @Test
    @DisplayName("기존 피드백 생성 이후에 게시글이 수정 되었으면 새로운 피드백 데이터가 반환된다.")
    void getStudyFeedbackWhenBoardUpdated() {
        // given
        User user = new User();
        user.setEmail("email");
        user.setName("name");
        user.setPwd("pwd");
        user.setRole("role");
        user.setNickname("nickName");
        user.setPhone_number("010-0000-0000");
        userRepository.save(user);

        Board board = boardRepository.save(Board.builder()
            .user(user)
            .title("title")
            .content("content")
            .build());

        feedbackRepository.save(Feedback.builder()
            .board(board)
            .feedback("feedback")
            .futureLearningStrategy("futureLearningStrategy")
            .build());

        board.setContent("new content");
        boardRepository.save(board);

        OpenAiStudyFeedbackResponse mockResponse = new OpenAiStudyFeedbackResponse();
        Mockito.doReturn(mockResponse).when(openAiService).getStudyFeedback(anyString(), anyString());

        // when
        StudyFeedbackResponse response = boardServiceImpl.getStudyFeedback(board.getId());

        // then
        assertThat(response.getFeedback()).isNull();
        assertThat(response.getFutureLearningStrategy()).isNull();
        verify(openAiService, times(1)).getStudyFeedback(anyString(), anyString());
    }

    @Test
    @DisplayName("기존 피드백 생성 이후에 게시글이 수정되지 않았으면 기존 피드백 데이터가 반환된다.")
    void getStudyFeedbackWhenBoardNotUpdated() {
        // given
        User user = new User();
        user.setEmail("email");
        user.setName("name");
        user.setPwd("pwd");
        user.setRole("role");
        user.setNickname("nickName");
        user.setPhone_number("010-0000-0000");
        userRepository.save(user);

        Board board = boardRepository.save(Board.builder()
            .user(user)
            .title("title")
            .content("content")
            .build());

        Feedback feedback = feedbackRepository.save(Feedback.builder()
            .board(board)
            .feedback("feedback")
            .futureLearningStrategy("futureLearningStrategy")
            .build());

        OpenAiStudyFeedbackResponse mockResponse = new OpenAiStudyFeedbackResponse();
        Mockito.doReturn(mockResponse).when(openAiService).getStudyFeedback(anyString(), anyString());

        // when
        StudyFeedbackResponse response = boardServiceImpl.getStudyFeedback(board.getId());

        // then
        assertThat(response.getFeedback()).isEqualTo(feedback.getFeedback());
        assertThat(response.getFutureLearningStrategy()).isEqualTo(feedback.getFutureLearningStrategy());
        verify(openAiService, times(0)).getStudyFeedback(anyString(), anyString());
    }
}