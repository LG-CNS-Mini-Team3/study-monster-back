package com.example.study_monster_back.comment.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.comment.dto.Comment_INOUT;
import com.example.study_monster_back.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public ResponseEntity<String> addComment(@RequestBody Comment_INOUT dto){
        
        commentService.createComment(dto);

        return ResponseEntity.ok("추가 완료");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResponseEntity<List<Comment_INOUT>> listComment(@RequestParam Long boardId){
        List<Comment_INOUT> list = commentService.getComments(boardId);
        return ResponseEntity.ok(list);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    @ResponseBody
    public ResponseEntity<String> deleteComment(@RequestBody Comment_INOUT comment){

        commentService.deleteComment(comment);

        return ResponseEntity.ok("삭제 완료");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/modify")
    public ResponseEntity<String> updateComment(@RequestBody Comment_INOUT dto){

        commentService.modifyComment(dto);


        return ResponseEntity.ok("수정 완료");
    }

}
