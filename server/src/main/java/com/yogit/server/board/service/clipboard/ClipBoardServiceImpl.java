package com.yogit.server.board.service.clipboard;

import com.yogit.server.board.dto.request.clipboard.*;
import com.yogit.server.board.dto.response.clipboard.ClipBoardRes;
import com.yogit.server.board.dto.response.clipboard.GetClipBoardRes;
import com.yogit.server.board.dto.response.comment.CommentRes;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.entity.Comment;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.exception.clipboard.NotFoundClipBoardException;
import com.yogit.server.board.exception.clipboard.NotUserOfClipBoardException;
import com.yogit.server.board.exception.comment.NotFoundCommentException;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.board.repository.ClipBoardRepository;
import com.yogit.server.board.repository.CommentRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.entity.User;
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
public class ClipBoardServiceImpl implements ClipBoardService{

    private final ClipBoardRepository clipBoardRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<ClipBoardRes> createClipBoard(CreateClipBoardReq dto){

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        // ClipBoard객체 생성
        ClipBoard clipBoard = new ClipBoard(dto, user, board);
        ClipBoard savedClipBoard = clipBoardRepository.save(clipBoard); // 생성 요청
        ClipBoardRes clipBoardRes = ClipBoardRes.toDto(savedClipBoard);// resDto로 변환
        return ApplicationResponse.create("클립보드 생성에 성공하였습니다.", clipBoardRes);
    }


    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<GetClipBoardRes> findClipBoard(GetClipBoardReq dto){

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        ClipBoard clipBoard = clipBoardRepository.findClipBoardById(dto.getClipBoardId())
                .orElseThrow(() -> new NotFoundClipBoardException());

        List<CommentRes> comments = commentRepository.findAllCommentsByClipBoardId(clipBoard.getId()).stream()
                .map(comment -> CommentRes.toDto(comment))
                .collect(Collectors.toList());

        // 코멘트 추가
        GetClipBoardRes getClipBoardRes = GetClipBoardRes.toDto(clipBoard, comments);
        return ApplicationResponse.ok(getClipBoardRes);
    }


    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<List<GetClipBoardRes>> findAllClipBoards(GetAllClipBoardsReq dto){

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        // 클립보드 res안에 해당하는 코멘트 리스트까지 조회 및 포함
        List<GetClipBoardRes> getClipBoardResList = clipBoardRepository.findAllByBoardId(dto.getBoardId()).stream()
                .map(clipBoard -> GetClipBoardRes.toDto(clipBoard, commentRepository.findAllCommentsByClipBoardId(clipBoard.getId()).stream()
                        .map(comment -> CommentRes.toDto(comment))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

        return ApplicationResponse.ok(getClipBoardResList);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<ClipBoardRes> updateClipBoard(PatchClipBoardReq dto){
        //필요 객체 조회
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        ClipBoard clipBoard = clipBoardRepository.findClipBoardById(dto.getClipBoardId())
                .orElseThrow(() -> new NotFoundClipBoardException());

        //Validation: 요청 사용자와 클립보드 생성자가 같은지 검증
        if(!user.getId().equals(clipBoard.getUser().getId())){
            throw new NotUserOfClipBoardException();
        }

        clipBoard.updateClipBoard(dto); // 업데이트
        ClipBoardRes clipBoardRes = ClipBoardRes.toDto(clipBoard);
        return ApplicationResponse.ok(clipBoardRes);
    }

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<ClipBoardRes> deleteClipBoard(DeleteClipBoardReq dto){
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        ClipBoard clipBoard = clipBoardRepository.findClipBoardById(dto.getClipBoardId())
                .orElseThrow(() -> new NotFoundClipBoardException());

        //Validation: 요청 사용자와 클립보드 생성자가 같은지 검증
        if(!user.getId().equals(clipBoard.getUser().getId())){
            throw new NotUserOfClipBoardException();
        }

        clipBoard.deleteClipBoard();// 삭제
        ClipBoardRes clipBoardRes = ClipBoardRes.toDto(clipBoard);
        return ApplicationResponse.ok(clipBoardRes);
    }
}
