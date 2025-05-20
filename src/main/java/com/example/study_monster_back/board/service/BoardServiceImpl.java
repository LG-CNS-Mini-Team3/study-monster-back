package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.response.StudyFeedbackResponse;
import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.openAi.service.OpenAiService;
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
        User user = userRepository.findById(boardRequestDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + boardRequestDto.getUserId()));

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
    public StudyFeedbackResponse getStudyFeedback(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
            new RuntimeException("해당 id를 가진 게시글이 없습니다.")
        );
        return new StudyFeedbackResponse(openAiService.getStudyFeedback(board.getTitle(), board.getContent()));
    }
}
