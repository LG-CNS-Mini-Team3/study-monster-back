package com.example.study_monster_back.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.dto.db.BoardDetailInfo;

import java.util.Optional;
import com.example.study_monster_back.board.dto.db.BoardDetailInfo;
import com.example.study_monster_back.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("""
    SELECT b board, u writer
    FROM Board b
    LEFT JOIN User u ON (b.user = u)
    where b.id = :boardId
    """)
    Optional<BoardDetailInfo> getBoardByIdWithUser(Long boardId);

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.boardTags bt LEFT JOIN FETCH bt.tag Where b.id = :id")
    Optional<Board> findByIdWithTags(@Param("id") Long id);
           
    Page<Board> findByTitleContaining(String title, Pageable pageable);

    Page<Board> findByContentContaining(String content, Pageable pageable);

    @Query("SELECT b FROM Board b JOIN b.user u WHERE LOWER(u.nickname) LIKE LOWER(CONCAT('%', :nickname, '%'))")
    Page<Board> findByWriterContaining(@Param("nickname") String nickname, Pageable pageable);

    @Query("SELECT b FROM Board b JOIN b.user u " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Board> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
