package com.example.study_monster_back.board.repository;

import com.example.study_monster_back.board.dto.db.BoardDetailInfo;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("board 리포지토리로 ")
class BoardRepositoryTest {
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
    void getBoardByIdWithUser() {
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
        Optional<BoardDetailInfo> result = boardRepository.getBoardByIdWithUser(board.getId());

        // then
        assertThat(result)
            .isPresent()
            .hasValueSatisfying(boardDetailInfo -> {
                assertThat(boardDetailInfo.getBoard())
                    .extracting("title", "content", "user.id")
                    .containsExactly(board.getTitle(), board.getContent(), board.getUser().getId());
                assertThat(boardDetailInfo.getWriter())
                    .extracting("nickname", "email", "name")
                    .containsExactly(user.getNickname(), user.getEmail(), user.getName());
            });
    }
}