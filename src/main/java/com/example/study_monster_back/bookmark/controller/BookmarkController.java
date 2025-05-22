package com.example.study_monster_back.bookmark.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.bookmark.dto.Bookmark_IN;
import com.example.study_monster_back.bookmark.dto.Bookmark_OUT;
import com.example.study_monster_back.bookmark.service.BookmarkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
@CrossOrigin
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<String> addBookmark(@RequestBody Bookmark_IN dto) {
        boolean result = bookmarkService.addBookmark(dto);
        return result
            ? ResponseEntity.ok("북마크 추가 완료")
            : ResponseEntity.status(HttpStatus.CONFLICT).body("이미 북마크되어 있음");
    }

    @DeleteMapping
    public ResponseEntity<String> removeBookmark(@RequestBody Bookmark_IN dto) {
        boolean result = bookmarkService.removeBookmark(dto);
        return result
            ? ResponseEntity.ok("북마크 해제 완료")
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("북마크 내역 없음");
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkBookmark(@RequestBody Bookmark_IN dto) {
        return ResponseEntity.ok(bookmarkService.isBookmarked(dto));
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<Bookmark_OUT>> getBookmarks(@PathVariable Long userId) {
        List<Bookmark_OUT> result = bookmarkService.getBookmarks(userId);
        return ResponseEntity.ok(result);
    }
}

