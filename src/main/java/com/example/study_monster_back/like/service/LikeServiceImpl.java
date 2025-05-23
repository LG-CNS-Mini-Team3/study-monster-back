package com.example.study_monster_back.like.service;

import org.springframework.stereotype.Service;

import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.like.dto.LikeCount_OUT;
import com.example.study_monster_back.like.dto.Like_IN;
import com.example.study_monster_back.like.entity.Like;
import com.example.study_monster_back.like.repository.LikeRepository;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public void chu(Like_IN dto){
        User user = userRepository.findById(dto.getUser_id()).orElseThrow(() -> new RuntimeException("사용자가 삭제되었거나 존재하지 않습니다"));
        Board board = boardRepository.findById(dto.getBoard_id()).orElseThrow(() -> new RuntimeException("게시글이 삭제되었거나 존재하지 않습니다"));
        Like like = new Like();
        like.setUser(user);
        like.setBoard(board);

        if(dto.getIs_dislike() == 1){
            like.setIsDislike(true);
        } else {
            like.setIsDislike(false);
        }
        
        likeRepository.save(like);
    
    }
    public LikeCount_OUT getCount(Long boardId){

        long like = likeRepository.countByBoardIdAndIsDislike(boardId, false);
        long dislike = likeRepository.countByBoardIdAndIsDislike(boardId, true);

        LikeCount_OUT LikeCountDTO = new LikeCount_OUT(like, dislike);


        return LikeCountDTO;
    }

}
