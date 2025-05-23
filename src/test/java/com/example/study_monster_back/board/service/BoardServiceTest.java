package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.request.UpdateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.comment.entity.Comment;
import com.example.study_monster_back.comment.repository.CommentRepository;
import com.example.study_monster_back.feedback.entity.Feedback;
import com.example.study_monster_back.feedback.repository.FeedbackRepository;
import com.example.study_monster_back.like.entity.Like;
import com.example.study_monster_back.like.repository.LikeRepository;
import com.example.study_monster_back.tag.dto.response.TagResponseDto;
import com.example.study_monster_back.tag.repository.BoardTagRepository;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    private BoardTagRepository boardTagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private LikeRepository likeRepository;

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
        CreateBoardResponseDto responseDto = boardService.createBoard(requestDto, "1L");

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


    @Test
    void 게시글_수정_시_제목_내용_태그가_정상적으로_변경된다() {
        // given
        User user = createAndSaveUser();

        CreateBoardRequestDto createDto = new CreateBoardRequestDto(
                "원래 제목",
                "원래 내용",
                user.getId(),
                Arrays.asList("original1", "original2")
        );
        CreateBoardResponseDto createdBoard = boardService.createBoard(createDto, "1L");

        UpdateBoardRequestDto updateDto = new UpdateBoardRequestDto(
                "수정된 제목",
                "수정된 내용",
                user.getId(),
                Arrays.asList("original1", "change1") // original2 → 제거, change1 → 추가
        );

        // when
        boardService.updateBoard(createdBoard.getId(), updateDto, "1L");

        // then
        Board updatedBoard = boardRepository.findByIdWithTags(createdBoard.getId())
                .orElseThrow(() -> new AssertionError("게시글이 수정되지 않았습니다."));

        assertThat(updatedBoard.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedBoard.getContent()).isEqualTo("수정된 내용");

        List<String> tagNames = updatedBoard.getBoardTags().stream()
                .map(bt -> bt.getTag().getName())
                .toList();

        assertThat(tagNames.size()).isEqualTo(2);
        assertTrue(tagNames.contains("original1"));
        assertTrue(tagNames.contains("change1"));
        assertFalse(tagNames.contains("original2"));
    }


    @Test
    void 게시글_삭제_시_연관된_데이터도_함께_삭제된다() {
        // given
        User user = createAndSaveUser();

        CreateBoardRequestDto requestDto = new CreateBoardRequestDto(
                "삭제 테스트 제목",
                "삭제 테스트 내용",
                user.getId(),
                Arrays.asList("deleteTest1", "deleteTest2")
        );
        CreateBoardResponseDto created = boardService.createBoard(requestDto, "1L");
        Long boardId = created.getId();

        Board board = boardRepository.findById(boardId).orElseThrow();

        commentRepository.save(new Comment(null, "댓글1", LocalDateTime.now(), LocalDateTime.now(), user, board));
        commentRepository.save(new Comment(null, "댓글2", LocalDateTime.now(), LocalDateTime.now(), user, board));
        likeRepository.save(new Like(null, false, user, board));
        feedbackRepository.save(new Feedback(null, "피드백1", "피드백2", LocalDateTime.now(), board));

        assertThat(boardRepository.findById(boardId)).isNotEmpty();
        assertThat(boardTagRepository.countByBoard(board)).isEqualTo(2);
        assertThat(commentRepository.countByBoard(board)).isEqualTo(2);
        assertThat(likeRepository.countByBoard(board)).isEqualTo(1);
        assertThat(feedbackRepository.countByBoard(board)).isEqualTo(1);

        // when
        boardService.deleteBoard(boardId, "1L");

        // then
        assertThat(boardRepository.findById(boardId)).isEmpty();
        assertThat(boardTagRepository.countByBoard(board)).isEqualTo(0);
        assertThat(commentRepository.countByBoard(board)).isEqualTo(0);
        assertThat(likeRepository.countByBoard(board)).isEqualTo(0);
        assertThat(feedbackRepository.countByBoard(board)).isEqualTo(0);
    }

    @Test
    void 게시글Id로_해당_게시글의_태그를_조회한다() {
        // given
        User user = createAndSaveUser();

        CreateBoardRequestDto requestDto = new CreateBoardRequestDto(
                "해당 게시글의 태그 조회",
                "해당 게시글의 태그 조회",
                user.getId(),
                Arrays.asList("get_tag1", "get_tag2")
        );
        CreateBoardResponseDto created = boardService.createBoard(requestDto, "1L");


        // when
        List<TagResponseDto> tagResponseDtoList = boardService.getBoardTags(created.getId());

        // then
        List<String> tagNames = tagResponseDtoList.stream()
                .map(tagResponseDto -> tagResponseDto.getName())
                .toList();

        assertThat(tagNames.size()).isEqualTo(2);
        assertTrue(tagNames.contains("get_tag1"));
        assertTrue(tagNames.contains("get_tag2"));
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