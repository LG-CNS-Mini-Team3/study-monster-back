package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.response.StudyFeedbackResponse;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.openAi.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final OpenAiService openAiService;

    @Override
    public StudyFeedbackResponse getStudyFeedback(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
            new RuntimeException("해당 id를 가진 게시글이 없습니다.")
        );
        return new StudyFeedbackResponse(openAiService.getStudyFeedback(board.getTitle(), board.getContent()));
    }
}
