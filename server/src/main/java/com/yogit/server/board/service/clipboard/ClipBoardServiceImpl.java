package com.yogit.server.board.service.clipboard;

import com.yogit.server.block.repository.BlockRepository;
import com.yogit.server.board.dto.request.clipboard.*;
import com.yogit.server.board.dto.response.clipboard.ClipBoardRes;
import com.yogit.server.board.dto.response.clipboard.GetClipBoardRes;
import com.yogit.server.board.dto.response.comment.CommentRes;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.exception.clipboard.NotFoundClipBoardException;
import com.yogit.server.board.exception.clipboard.NotUserOfClipBoardException;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.board.repository.ClipBoardRepository;
import com.yogit.server.board.repository.CommentRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.s3.AwsS3Service;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import com.yogit.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final AwsS3Service awsS3Service;
    private final BlockRepository blockRepository;
    private final UserService userService;

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<ClipBoardRes> createClipBoard(CreateClipBoardReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        // ClipBoard객체 생성
        ClipBoard clipBoard = new ClipBoard(dto, user, board);
        ClipBoard savedClipBoard = clipBoardRepository.save(clipBoard); // 생성 요청

        List<CommentRes> comments = commentRepository.findAllCommentsByClipBoardId(clipBoard.getId()).stream()
                .map(comment -> CommentRes.toDto(comment))
                .collect(Collectors.toList());

        String profileImgUrl = awsS3Service.makeUrlOfFilename(user.getProfileImg());// 유저 프로필 사진 multipart -> url 로 변환

        ClipBoardRes res = ClipBoardRes.toDto(savedClipBoard, comments, profileImgUrl);// resDto로 변환
        return ApplicationResponse.create("클립보드 생성에 성공하였습니다.", res);
    }


    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<GetClipBoardRes> findClipBoard(GetClipBoardReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        ClipBoard clipBoard = clipBoardRepository.findClipBoardById(dto.getClipBoardId())
                .orElseThrow(() -> new NotFoundClipBoardException());

        List<CommentRes> comments = commentRepository.findAllCommentsByClipBoardId(clipBoard.getId()).stream()
                .map(comment -> CommentRes.toDto(comment))
                .collect(Collectors.toList());

        String profileImgUrl = awsS3Service.makeUrlOfFilename(user.getProfileImg());// 유저 프로필 사진 multipart -> url 로 변환

        // 코멘트 추가
        GetClipBoardRes getClipBoardRes = GetClipBoardRes.toDto(clipBoard, comments, profileImgUrl);
        return ApplicationResponse.ok(getClipBoardRes);
    }


//    @Transactional(readOnly = true)
//    @Override
//    public ApplicationResponse<List<GetClipBoardRes>> findAllClipBoards(GetAllClipBoardsReq dto){
//
//        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());
//
//        User user = userRepository.findByUserId(dto.getUserId())
//                .orElseThrow(() -> new NotFoundUserException());
//
//        Board board = boardRepository.findBoardById(dto.getBoardId())
//                .orElseThrow(() -> new NotFoundBoardException());
//
//        List<User> blockedUsers = blockRepository.findBlocksByBlockingUserId(dto.getUserId()).stream()
//                .map(block -> block.getBlockedUser())
//                .collect(Collectors.toList());
//
//        // 클립보드 res안에 해당하는 코멘트 리스트까지 조회 및 포함
//        // 유저 profileImgUrl 또한 img uuid -> s3 url로 변환
//        List<GetClipBoardRes> getClipBoardResList = clipBoardRepository.findAllByBoardId(dto.getBoardId()).stream()
//                .filter(clipBoard -> !blockedUsers.contains(clipBoard.getUser())) // 차단당한 유저의 데이터 제외
//                .map(clipBoard -> GetClipBoardRes.toDto(clipBoard, commentRepository.findAllCommentsByClipBoardId(clipBoard.getId()).stream()
//                        .filter(comment -> !blockedUsers.contains(comment.getUser()))
//                        .map(comment -> CommentRes.toDto(comment))
//                        .collect(Collectors.toList()),
//                        awsS3Service.makeUrlOfFilename(clipBoard.getUser().getProfileImg())))
//                .collect(Collectors.toList());
//
//        return ApplicationResponse.ok(getClipBoardResList);
//    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<List<GetClipBoardRes>> findAllClipBoards(GetAllClipBoardsReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        int cursor = dto.getCursor();
        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        List<User> blockedUsers = blockRepository.findBlocksByBlockingUserId(dto.getUserId()).stream()
                .map(block -> block.getBlockedUser())
                .collect(Collectors.toList());

        // jpa 다중 정렬 order
        Sort sort = Sort.by(
                Sort.Order.asc("createdAt")
        );
        PageRequest pageRequest = PageRequest.of(cursor, 10, sort); // 페이징 요청 객체

        // 클립보드 res안에 해당하는 코멘트 리스트까지 조회 및 포함
        // 유저 profileImgUrl 또한 img uuid -> s3 url로 변환
        List<GetClipBoardRes> getClipBoardResList = clipBoardRepository.findClipBoardsByBoardId(pageRequest, dto.getBoardId()).stream()
                .filter(clipBoard -> !blockedUsers.contains(clipBoard.getUser())) // 차단당한 유저의 데이터 제외
                .map(clipBoard -> GetClipBoardRes.toDto(clipBoard, commentRepository.findAllCommentsByClipBoardId(clipBoard.getId()).stream()
                                .filter(comment -> !blockedUsers.contains(comment.getUser()))
                                .map(comment -> CommentRes.toDto(comment))
                                .collect(Collectors.toList()),
                        awsS3Service.makeUrlOfFilename(clipBoard.getUser().getProfileImg())))
                .collect(Collectors.toList());

        return ApplicationResponse.ok(getClipBoardResList);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<ClipBoardRes> updateClipBoard(PatchClipBoardReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        //필요 객체 조회
        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        ClipBoard clipBoard = clipBoardRepository.findClipBoardById(dto.getClipBoardId())
                .orElseThrow(() -> new NotFoundClipBoardException());

        //Validation: 요청 사용자와 클립보드 생성자가 같은지 검증
        if(!user.getId().equals(clipBoard.getUser().getId())){
            throw new NotUserOfClipBoardException();
        }

        clipBoard.updateClipBoard(dto); // 업데이트

        List<CommentRes> comments = commentRepository.findAllCommentsByClipBoardId(clipBoard.getId()).stream()
                .map(comment -> CommentRes.toDto(comment))
                .collect(Collectors.toList());

        String profileImgUrl = awsS3Service.makeUrlOfFilename(user.getProfileImg());// 유저 프로필 사진 multipart -> url 로 변환

        ClipBoardRes clipBoardRes = ClipBoardRes.toDto(clipBoard, comments, profileImgUrl);
        return ApplicationResponse.ok(clipBoardRes);
    }

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<String> deleteClipBoard(DeleteClipBoardReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        ClipBoard clipBoard = clipBoardRepository.findClipBoardById(dto.getClipBoardId())
                .orElseThrow(() -> new NotFoundClipBoardException());

        //Validation: 요청 사용자와 클립보드 생성자가 같은지 검증
        if(!user.getId().equals(clipBoard.getUser().getId())){
            throw new NotUserOfClipBoardException();
        }

        clipBoard.deleteClipBoard();// 삭제
//        ClipBoardRes clipBoardRes = ClipBoardRes.toDto(clipBoard);
        return ApplicationResponse.ok("클립 보드가 삭제되었습니다.");
    }
}
