package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.db.BoardDetailInfo;
import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("board 서비스로 ")
class BoardServiceTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
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
        GetBoardResponseDto response = boardService.getBoard(board.getId());

        // then
        assertThat(response)
            .extracting("boardId","title","content","userId","email","nickname")
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
            () -> boardService.getBoard(1L));

        // then
        assertThat(runtimeException.getMessage())
            .isEqualTo("해당 id를 가진 게시글이 없습니다.");
    }
}