package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.response.StudyFeedbackResponse;
import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.request.UpdateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.dto.response.UpdateBoardResponseDto;
import com.example.study_monster_back.tag.dto.response.TagResponseDto;
import com.example.study_monster_back.user.repository.UserRepository;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.comment.repository.CommentRepository;
import com.example.study_monster_back.feedback.repository.FeedbackRepository;
import com.example.study_monster_back.like.repository.LikeRepository;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.feedback.entity.Feedback;
import com.example.study_monster_back.tag.entity.BoardTag;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.tag.entity.Tag;
import com.example.study_monster_back.openAi.dto.response.OpenAiStudyFeedbackResponse;
import com.example.study_monster_back.openAi.service.OpenAiService;
import com.example.study_monster_back.tag.service.BoardTagService;
import com.example.study_monster_back.tag.service.TagService;
import com.example.study_monster_back.tag.util.TagValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

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
    private final OpenAiService openAiService;

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
    public UpdateBoardResponseDto updateBoard(Long boardId, UpdateBoardRequestDto boardRequestDto) {

        // TODO: userId 시큐리티컨텍스트에서 받아서 자신의 게시글이 맞는지 확인해야 함.
        Board board = boardRepository.findByIdWithTags(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with ID: " + boardId));

        User user = getUserOrThrow(boardRequestDto.getUserId());
        if(!board.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("게시글을 작성한 사용자만 게시글을 수정할 수 있습니다.");
        }

        board.updateBoardTitleAndContent(boardRequestDto);

        Set<String> updateTagNames =  tagValidator.filterValidTags(boardRequestDto.getTags());
        Set<String> existingTagNames = board.getBoardTags().stream()
                .map(bt -> bt.getTag().getName())
                .collect(Collectors.toSet());

        for (BoardTag boardTag : new ArrayList<>(board.getBoardTags())) {
            if (!updateTagNames.contains(boardTag.getTag().getName())) {
                board.removeBoardTag(boardTag);
            }
        }

        for (String tagName : updateTagNames) {
            if (!existingTagNames.contains(tagName)) {
                Tag tag = tagService.findOrCreateTag(tagName);
                BoardTag boardTag = boardTagService.createBoardTag(board, tag);
                board.addBoardTag(boardTag);
            }
        }

        return UpdateBoardResponseDto.from(board);

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

    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDto> getBoardTags(Long boardId) {

        Board board = boardRepository.findByIdWithTags(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with ID: " + boardId));

        List<TagResponseDto> tagResponseDtoList = board.getBoardTags().stream()
                .map(bt -> TagResponseDto.from(bt.getTag()))
                .collect(Collectors.toList());

        return tagResponseDtoList;
    }

    public StudyFeedbackResponse getStudyFeedback(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
            new RuntimeException("해당 id를 가진 게시글이 없습니다.")
        );
        Optional<Feedback> optionalFeedback = feedbackRepository.findByBoard(board);
        if (optionalFeedback.isEmpty() || optionalFeedback.get().getCreated_at().isBefore(board.getUpdated_at())) {
            OpenAiStudyFeedbackResponse studyFeedback = openAiService.getStudyFeedback(board.getTitle(), board.getContent());
            Feedback feedback = feedbackRepository.save(
                Feedback.builder()
                    .board(board)
                    .feedback(studyFeedback.getFeedback())
                    .futureLearningStrategy(studyFeedback.getFutureLearningStrategy())
                    .build()
            );
            optionalFeedback = Optional.of(feedback);
        }
        return new StudyFeedbackResponse(optionalFeedback.get());

    }
  
    private User getUserOrThrow(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return user;
    }
}
