package com.example.study_monster_back.like.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.like.dto.LikeCount_OUT;
import com.example.study_monster_back.like.dto.Like_IN;
import com.example.study_monster_back.like.service.LikeService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public ResponseEntity<String> addLike(@RequestBody Like_IN dto) {
        System.out.println(dto.getUser_id());
        System.out.println(dto.getBoard_id());
        likeService.chu(dto);

        return ResponseEntity.ok("추가 완료");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/{boardId}")
    public ResponseEntity<LikeCount_OUT> getCount(@PathVariable(value = "boardId") Long boardId) {

        LikeCount_OUT count = likeService.getCount(boardId);

        return ResponseEntity.ok(count);
    }
}
