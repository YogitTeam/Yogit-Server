package com.yogit.server.board.service.clipboard;

import com.yogit.server.apns.dto.req.CreateBoardUserJoinAPNReq;
import com.yogit.server.apns.dto.req.CreateClipBoardAPNReq;
import com.yogit.server.apns.service.APNService;
import com.yogit.server.block.repository.BlockRepository;
import com.yogit.server.board.dto.request.clipboard.*;
import com.yogit.server.board.dto.response.clipboard.ClipBoardRes;
import com.yogit.server.board.dto.response.clipboard.GetClipBoardRes;
import com.yogit.server.board.dto.response.clipboard.GetClipBoardsRes;
import com.yogit.server.board.dto.response.comment.CommentRes;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.exception.clipboard.NotFoundClipBoardException;
import com.yogit.server.board.exception.clipboard.NotUserOfClipBoardException;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.board.repository.ClipBoardRepository;
import com.yogit.server.board.repository.CommentRepository;
import com.yogit.server.config.domain.BaseStatus;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.s3.AwsS3Service;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import com.yogit.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;
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
    private final APNService apnService;

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<ClipBoardRes> createClipBoard(CreateClipBoardReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        // ClipBoard?????? ??????
        ClipBoard clipBoard = new ClipBoard(dto, user, board);
        ClipBoard savedClipBoard = clipBoardRepository.save(clipBoard); // ?????? ??????

        List<CommentRes> comments = commentRepository.findAllCommentsByClipBoardId(clipBoard.getId()).stream()
                .map(comment -> CommentRes.toDto(comment))
                .collect(Collectors.toList());

        String profileImgUrl = awsS3Service.makeUrlOfFilename(user.getProfileImg());// ?????? ????????? ?????? multipart -> url ??? ??????

        //Board??????????????? ClipBOard ?????? ??????
        List<BoardUser> boardUsers = board.getBoardUsers().stream()
                .filter(boardUser -> boardUser.getUser().getStatus().equals(BaseStatus.ACTIVE))
                .collect(Collectors.toList());
        if(boardUsers!=null){
            for(BoardUser bu: boardUsers){
                try {
                    apnService.createClipBoardAPN(new CreateClipBoardAPNReq(bu.getUser().getDeviceToken(), user.getName(), board.getId(), board.getTitle()));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        ClipBoardRes res = ClipBoardRes.toDto(savedClipBoard, comments, profileImgUrl);// resDto??? ??????
        return ApplicationResponse.create("???????????? ????????? ?????????????????????.", res);
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

        String profileImgUrl = awsS3Service.makeUrlOfFilename(user.getProfileImg());// ?????? ????????? ?????? multipart -> url ??? ??????

        // ????????? ??????
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
//        // ???????????? res?????? ???????????? ????????? ??????????????? ?????? ??? ??????
//        // ?????? profileImgUrl ?????? img uuid -> s3 url??? ??????
//        List<GetClipBoardRes> getClipBoardResList = clipBoardRepository.findAllByBoardId(dto.getBoardId()).stream()
//                .filter(clipBoard -> !blockedUsers.contains(clipBoard.getUser())) // ???????????? ????????? ????????? ??????
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
    public ApplicationResponse<GetClipBoardsRes> findAllClipBoards(GetAllClipBoardsReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        int cursor = dto.getCursor();
        int totalPage=0;
        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        List<User> blockedUsers = blockRepository.findBlocksByBlockingUserId(dto.getUserId()).stream()
                .map(block -> block.getBlockedUser())
                .collect(Collectors.toList());

        // jpa ?????? ?????? order
        Sort sort = Sort.by(
                Sort.Order.asc("createdAt")
        );
        PageRequest pageRequest = PageRequest.of(cursor, 10, sort); // ????????? ?????? ??????

        // ???????????? res?????? ???????????? ????????? ??????????????? ?????? ??? ??????
        // ?????? profileImgUrl ?????? img uuid -> s3 url??? ??????
        List<GetClipBoardRes> getClipBoardResList = clipBoardRepository.findClipBoardsByBoardId(pageRequest, dto.getBoardId()).stream()
                .filter(clipBoard -> !blockedUsers.contains(clipBoard.getUser())) // ???????????? ????????? ????????? ??????
                .map(clipBoard -> GetClipBoardRes.toDto(clipBoard, commentRepository.findAllCommentsByClipBoardId(clipBoard.getId()).stream()
                                .filter(comment -> !blockedUsers.contains(comment.getUser()))
                                .map(comment -> CommentRes.toDto(comment))
                                .collect(Collectors.toList()),
                        awsS3Service.makeUrlOfFilename(clipBoard.getUser().getProfileImg())))
                .collect(Collectors.toList());

        // totalPage ?????????
        Page<ClipBoard> getClipBoardReses = clipBoardRepository.findClipBoardsByBoardIdWithTotalPage(pageRequest, dto.getBoardId());
        totalPage = getClipBoardReses.getTotalPages();

        return ApplicationResponse.ok(GetClipBoardsRes.toDto(getClipBoardResList, totalPage));
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<ClipBoardRes> updateClipBoard(PatchClipBoardReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        //?????? ?????? ??????
        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        ClipBoard clipBoard = clipBoardRepository.findClipBoardById(dto.getClipBoardId())
                .orElseThrow(() -> new NotFoundClipBoardException());

        //Validation: ?????? ???????????? ???????????? ???????????? ????????? ??????
        if(!user.getId().equals(clipBoard.getUser().getId())){
            throw new NotUserOfClipBoardException();
        }

        clipBoard.updateClipBoard(dto); // ????????????

        List<CommentRes> comments = commentRepository.findAllCommentsByClipBoardId(clipBoard.getId()).stream()
                .map(comment -> CommentRes.toDto(comment))
                .collect(Collectors.toList());

        String profileImgUrl = awsS3Service.makeUrlOfFilename(user.getProfileImg());// ?????? ????????? ?????? multipart -> url ??? ??????

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

        //Validation: ?????? ???????????? ???????????? ???????????? ????????? ??????
        if(!user.getId().equals(clipBoard.getUser().getId())){
            throw new NotUserOfClipBoardException();
        }

        clipBoard.deleteClipBoard();// ??????
//        ClipBoardRes clipBoardRes = ClipBoardRes.toDto(clipBoard);
        return ApplicationResponse.ok("?????? ????????? ?????????????????????.");
    }
}
