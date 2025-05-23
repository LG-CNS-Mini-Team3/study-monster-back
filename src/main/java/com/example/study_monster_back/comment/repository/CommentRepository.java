package com.example.study_monster_back.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.comment.entity.Comment;
import com.example.study_monster_back.user.entity.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByBoard(Board board);
    Long countByBoard(Board board);
    List<Comment> findByBoardId(Long boardId);

    Comment findByIdAndUser(Long id, User user);


}
