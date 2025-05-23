package com.example.study_monster_back.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.comment.dto.Comment_INOUT;
import com.example.study_monster_back.comment.entity.Comment;
import com.example.study_monster_back.comment.repository.CommentRepository;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    public void createComment(Comment_INOUT dto){
        System.out.println(dto);
        Board board = boardRepository.findById(dto.getBoard_id()).orElseThrow(() -> new RuntimeException("게시글이 삭제되었거나 존재하지 않습니다"));

        User user = userRepository.findById(dto.getUser_id()).orElseThrow(() -> new RuntimeException("게시글이 삭제되었거나 존재하지 않습니다"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setBoard(board);
        comment.setContent(dto.getContent());
        
        commentRepository.save(comment);
        
    }

    @Override
    public List<Comment_INOUT> getComments(Long boardId){
        List<Comment> list = commentRepository.findByBoardId(boardId);
        List<Comment_INOUT> list2 = list.stream().map(Comment_INOUT::new).collect(Collectors.toList());
        return list2;
    }

    @Override
    public void deleteComment(Comment_INOUT dto){
        Comment comment = getComment(dto.getUser_id(), dto.getId());
        if (comment == null){
            throw new IllegalArgumentException("댓글은 작성한 사람만이 삭제할 수 있습니다");
        }
        
        commentRepository.delete(comment);

    }

    @Override
    public void modifyComment(Comment_INOUT dto){
        Comment comment = getComment(dto.getUser_id(), dto.getId());
        if (comment == null){
            throw new IllegalArgumentException("댓글은 작성한 사람만이 수정할 수 있습니다");
        }
        if (dto.getContent().isBlank()){
            throw new IllegalArgumentException("댓글 내용을 입력해 주세요.");
        }
        comment.setContent(dto.getContent());

        commentRepository.save(comment);
    }

    public Comment getComment(Long userId, Long commentId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("게시글이 삭제되었거나 존재하지 않습니다"));

        Comment comment = commentRepository.findByIdAndUser(commentId, user);

        return comment;
    }

}
