package com.example.study_monster_back.bookmark.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.bookmark.dto.Bookmark_IN;
import com.example.study_monster_back.bookmark.dto.Bookmark_OUT;
import com.example.study_monster_back.bookmark.entity.Bookmark;
import com.example.study_monster_back.bookmark.repository.BookmarkRepository;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public boolean addBookmark(Bookmark_IN dto) {
        User user = userRepository.findById(dto.getUser_id()).orElseThrow(() -> new RuntimeException("사용자가 삭제되었거나 존재하지 않습니다"));
        Board board = boardRepository.findById(dto.getBoard_id()).orElseThrow(() -> new RuntimeException("게시글이 삭제되었거나 존재하지 않습니다"));

        if (bookmarkRepository.existsByUserAndBoard(user, board)) return false;

        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setBoard(board);
        bookmarkRepository.save(bookmark);
        return true;
    }

    public boolean removeBookmark(Bookmark_IN dto) {
        User user = userRepository.findById(dto.getUser_id()).orElseThrow(() -> new RuntimeException("사용자가 삭제되었거나 존재하지 않습니다"));
        Board board = boardRepository.findById(dto.getBoard_id()).orElseThrow(() -> new RuntimeException("게시글이 삭제되었거나 존재하지 않습니다"));

        Bookmark bookmark = bookmarkRepository.findByUserAndBoard(user, board);
        if (bookmark == null) return false;

        bookmarkRepository.delete(bookmark);
        return true;
    }

    public boolean isBookmarked(Bookmark_IN dto) {
        User user = userRepository.findById(dto.getUser_id()).orElseThrow(() -> new RuntimeException("사용자가 삭제되었거나 존재하지 않습니다"));
        Board board = boardRepository.findById(dto.getBoard_id()).orElseThrow(() -> new RuntimeException("게시글이 삭제되었거나 존재하지 않습니다"));
        return bookmarkRepository.existsByUserAndBoard(user, board);
    }

    public List<Bookmark_OUT> getBookmarks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자가 삭제되었거나 존재하지 않습니다"));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
        return bookmarks.stream()
                .map(b -> new Bookmark_OUT(b.getBoard()))
                .collect(Collectors.toList());
    }
}
