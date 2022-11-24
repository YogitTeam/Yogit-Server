package com.yogit.server.board.service.comment;

import com.yogit.server.board.dto.request.comment.CreateCommentReq;
import com.yogit.server.board.dto.request.comment.DeleteCommentReq;
import com.yogit.server.board.dto.request.comment.PatchCommentReq;
import com.yogit.server.board.dto.response.comment.DeleteCommentRes;
import com.yogit.server.board.dto.response.comment.CommentRes;
import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.entity.Comment;
import com.yogit.server.board.exception.clipboard.NotFoundClipBoardException;
import com.yogit.server.board.exception.comment.NotFoundCommentException;
import com.yogit.server.board.exception.comment.NotHostOfCommentException;
import com.yogit.server.board.repository.ClipBoardRepository;
import com.yogit.server.board.repository.CommentRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.InvalidTokenException;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ClipBoardRepository clipBoardRepository;



    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<CommentRes> createComment(CreateCommentReq dto){

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        ClipBoard clipBoard = clipBoardRepository.findClipBoardById(dto.getClipBoardId())
                .orElseThrow(() -> new NotFoundClipBoardException());

        Comment comment = new Comment(dto, user, clipBoard);
        Comment savedComment = commentRepository.save(comment);
        CommentRes commentRes = CommentRes.toDto(savedComment);
        return ApplicationResponse.create("코멘트 생성을 성공했습니다.", commentRes);
    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<List<CommentRes>> findAllComments(Long clipBoardId, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException());

        ClipBoard clipBoard = clipBoardRepository.findClipBoardById(clipBoardId)
                .orElseThrow(() -> new NotFoundClipBoardException());

        List<CommentRes> commentResList = commentRepository.findAllCommentsByClipBoardId(clipBoardId).stream()
                .map(comment -> CommentRes.toDto(comment))
                .collect(Collectors.toList());

        return ApplicationResponse.ok(commentResList);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<DeleteCommentRes> deleteComment(DeleteCommentReq dto, Long commentId){

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Comment comment = commentRepository.findCommentById(commentId)
                .orElseThrow(() -> new NotFoundCommentException());

        //검증: 요청 유저가 코멘트를 생성한 사람인지
        if(!user.getId().equals(comment.getUser().getId())){
            throw new NotHostOfCommentException();
        }

        comment.deleteComment();
        DeleteCommentRes deleteCommentRes = DeleteCommentRes.toDto(comment);
        return ApplicationResponse.ok(deleteCommentRes);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<CommentRes> updateComment(PatchCommentReq dto, Long commentId){

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Comment comment = commentRepository.findCommentById(dto.getCommentId())
                .orElseThrow(() -> new NotFoundCommentException());

        //검증: 요청 유저가 코멘트를 생성한 사람인지
        if(!user.getId().equals(comment.getUser().getId())){
            throw new NotHostOfCommentException();
        }

        comment.updateComment(dto);
        CommentRes commentRes = CommentRes.toDto(comment);
        return ApplicationResponse.ok(commentRes);
    }
}
