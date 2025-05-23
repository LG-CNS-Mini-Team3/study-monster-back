package com.example.study_monster_back.bookmark.service;

import java.util.List;

import com.example.study_monster_back.bookmark.dto.Bookmark_IN;
import com.example.study_monster_back.bookmark.dto.Bookmark_OUT;

public interface BookmarkService {
    public boolean addBookmark(Bookmark_IN dto);
    public boolean removeBookmark(Bookmark_IN dto);
    public boolean isBookmarked(Bookmark_IN dto);
    public List<Bookmark_OUT> getBookmarks(Long userId);
}
