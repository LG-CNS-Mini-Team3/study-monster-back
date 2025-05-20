package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.comment.repository.CommentRepository;
import com.example.study_monster_back.feedback.repository.FeedbackRepository;
import com.example.study_monster_back.like.entity.Like;
import com.example.study_monster_back.like.repository.LikeRepository;
import com.example.study_monster_back.tag.entity.BoardTag;
import com.example.study_monster_back.tag.entity.Tag;
import com.example.study_monster_back.tag.service.BoardTagService;
import com.example.study_monster_back.tag.service.TagService;
import com.example.study_monster_back.tag.util.TagValidator;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final FeedbackRepository feedbackRepository;
    private final TagService tagService;
    private final BoardTagService boardTagService;
    private final TagValidator tagValidator;

    public GetBoardResponseDto getBoard(Long boardId) {
        return GetBoardResponseDto.builder()
            .board(boardRepository.getBoardByIdWithUser(boardId).orElseThrow(
                () -> new RuntimeException("해당 id를 가진 게시글이 없습니다.")
            ).getBoard())
            .build();
    }

    @Transactional
    public CreateBoardResponseDto createBoard(CreateBoardRequestDto boardRequestDto) {

        // TODO: 추후 수정 예정(지금은 유저 정보를 dto에서 받아옴.)
        User user = getUserOrThrow(boardRequestDto.getUserId());

        Board board = Board.builder()
            .title(boardRequestDto.getTitle())
            .content(boardRequestDto.getContent())
            .user(user)
            .build();
        boardRepository.save(board);

        for (String tagName : tagValidator.filterValidTags(boardRequestDto.getTags())) {
            Tag tag = tagService.findOrCreateTag(tagName);
            BoardTag boardTag = boardTagService.createBoardTag(board, tag);
            board.addBoardTag(boardTag);
        }

        return CreateBoardResponseDto.from(board);
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with ID: " + boardId));
        // TODO: userId 시큐리티컨텍스트에서 받아서 자신의 게시글이 맞는지 확인해야 함.

        commentRepository.deleteAllByBoard(board);
        likeRepository.deleteAllByBoard(board);
        feedbackRepository.deleteAllByBoard(board);
        boardRepository.delete(board);
    }

    private User getUserOrThrow(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return user;
    }
}
