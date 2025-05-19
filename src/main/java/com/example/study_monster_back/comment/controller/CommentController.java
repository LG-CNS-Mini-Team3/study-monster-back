package com.example.study_monster_back.comment.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.comment.dto.CommentDTO;
import com.example.study_monster_back.comment.entity.Comment;
import com.example.study_monster_back.comment.repository.CommentRepository;
import com.example.study_monster_back.user.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.study_monster_back.board.repository.BoardRepository;

@RestController
public class CommentController {
    @Autowired CommentRepository commentRepository;
    @Autowired UserRepository userRepository;
    @Autowired BoardRepository boardRepository;

    @CrossOrigin
    @PostMapping("/comment/add")
    public ResponseEntity<?> addComment(@RequestBody Comment comment){
        comment.setUser(userRepository.findById((long) 1).get());
        comment.setBoard(boardRepository.findById((long) 1).get());
        commentRepository.save(comment);

        return ResponseEntity.ok("추가 완료");
    }

    @CrossOrigin
    @GetMapping("/comment/list")
    public List<CommentDTO> listComment(@RequestParam int board){

        List<Comment> list = commentRepository.findByBoardId(board);
        List<CommentDTO> list2 = list.stream().map(CommentDTO::new).collect(Collectors.toList());
        return list2;
    }

    @CrossOrigin
    @DeleteMapping("/comment/delete")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@RequestParam int board){
        commentRepository.deleteById(Long.valueOf(board));

        return ResponseEntity.ok("삭제 완료");
    }

    @CrossOrigin
    @PutMapping("/comment/modify")
    public ResponseEntity<?> updateComment(@RequestBody Comment comment){

        System.out.println(comment.getContent());

        Optional<Comment> temp = commentRepository.findById(comment.getId());

        if (temp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글을 찾을 수 없습니다.");
        }

        Comment newComment = temp.get();
        newComment.setContent(comment.getContent());
        commentRepository.save(newComment);


        return ResponseEntity.ok("수정 완료");
    }

}
