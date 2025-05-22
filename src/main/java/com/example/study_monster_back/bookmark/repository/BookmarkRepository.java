package com.example.study_monster_back.bookmark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.bookmark.entity.Bookmark;
import com.example.study_monster_back.user.entity.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{
    boolean existsByUserAndBoard(User user, Board board);
    void deleteByUserAndBoard(User user, Board board);
    Bookmark findByUserAndBoard(User user, Board board);
    List<Bookmark> findAllByUser(User user);
}
