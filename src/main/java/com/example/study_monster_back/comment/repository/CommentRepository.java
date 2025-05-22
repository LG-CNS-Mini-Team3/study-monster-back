package com.example.study_monster_back.comment.repository;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByBoard(Board board);
    Long countByBoard(Board board);
    List<Comment> findByBoardId(Long boardId);
}
