package com.example.study_monster_back;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.study_monster_back.board.dto.response.BoardResponse;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.board.service.BoardPagingService;
import com.example.study_monster_back.board.service.DelBoardByAdminService;
import com.example.study_monster_back.comment.entity.Comment;
import com.example.study_monster_back.comment.repository.CommentRepository;
import com.example.study_monster_back.comment.service.DelCommentByAdminService;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

@SpringBootTest
public class DHTests {
    @Autowired
    BoardPagingService boardPagingService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    DelCommentByAdminService delCommentByAdminService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void Paging() {
        int page = 0;
        int size = 5;
        String keyword = "자바";

        Pageable pageable = PageRequest.of(page, size);

        Page<BoardResponse> boardPage = boardPagingService.getBoards(keyword, pageable);

        assertThat(boardPage.getSize()).isEqualTo(size);
        assertThat(boardPage.getContent().size()).isLessThanOrEqualTo(size);
        System.out.println("총 게시글 수 : " + boardPage.getTotalElements());
        System.out.println("총 페이지 수 : " + boardPage.getTotalPages());
        boardPage.getContent().forEach(board -> {
            System.out.println(board.getId() + " - " + board.getTitle());
        });
    }

    @Test
    void delBoardByAdmin() {
        Long boardId = 1L;
        Long adminId = 100L;

        BoardRepository boardRepository = mock(BoardRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        DelBoardByAdminService DelBoardByAdminService = new DelBoardByAdminService(boardRepository, userRepository);

        User admin = new User();
        admin.setId(adminId);
        admin.setRole("admin");

        Board board = new Board();
        board.setId(boardId);

        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        assertDoesNotThrow(() -> DelBoardByAdminService.deleteBoardByAdmin(boardId, adminId));

        verify(boardRepository, times(1)).delete(board);
    }

    @Test
    void delCommentByAdmin() {
        Long commentId = 20L;
        Long adminId = 100L;

        CommentRepository commentRepository = mock(CommentRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        DelCommentByAdminService delCommentByAdminService = new DelCommentByAdminService(commentRepository,
                userRepository);

        User admin = new User();
        admin.setId(adminId);
        admin.setRole("admin");

        Comment comment = new Comment();
        comment.setId(adminId);

        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> delCommentByAdminService.deleteCommentByAdmin(commentId, adminId));

        verify(commentRepository, times(1)).delete(comment);
    }

}
