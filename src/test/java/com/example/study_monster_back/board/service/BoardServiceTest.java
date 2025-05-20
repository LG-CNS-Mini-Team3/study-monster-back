package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.tag.repository.TagRepository;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@DisplayName("BoardService 테스트")
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void 게시글_생성_요청시_게시글과_태그가_함께_저장된다() {
        // given
        User user = createAndSaveUser();

        CreateBoardRequestDto requestDto = new CreateBoardRequestDto(
                "테스트 제목",
                "테스트 내용",
                user.getId(),
                Arrays.asList("태그1", "태그2", "tag3")
        );

        // when
        CreateBoardResponseDto responseDto = boardService.createBoard(requestDto);

        // then
        // responseDto 검증
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(responseDto.getContent()).isEqualTo(requestDto.getContent());
        assertThat(responseDto.getUser().getUserId()).isEqualTo(user.getId());
        assertThat(responseDto.getTags().size()).isEqualTo(requestDto.getTags().size());

        // 저장된 게시글 검증
        Board savedBoard = boardRepository.findByIdWithTags(responseDto.getId())
                .orElseThrow(() -> new AssertionError("게시글이 저장되지 않았습니다."));

        assertThat(savedBoard.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(savedBoard.getContent()).isEqualTo(requestDto.getContent());
        assertThat(savedBoard.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedBoard.getBoardTags().size()).isEqualTo(responseDto.getTags().size());

        // 태그 검증
        List<String> tagNames = savedBoard.getBoardTags().stream()
                .map(boardTag -> boardTag.getTag().getName())
                .toList();

        assertTrue(tagNames.contains("태그1"));
        assertTrue(tagNames.contains("태그2"));
        assertTrue(tagNames.contains("tag3"));
    }


    private User createAndSaveUser() {

        User user = new User();
        user.setNickname("testUser");
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPwd("password");
        user.setRole("USER");
        user.setPhone_number("010-0000-5678");

        return userRepository.save(user);
    }
}