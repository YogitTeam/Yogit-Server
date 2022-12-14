package com.yogit.server.board.service;

import com.yogit.server.block.repository.BlockRepository;
import com.yogit.server.board.dto.request.*;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageReq;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageRes;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.board.dto.response.GetAllBoardRes;
import com.yogit.server.board.dto.response.GetBoardRes;
import com.yogit.server.board.entity.*;
import com.yogit.server.board.exception.InvalidMyClubTypeException;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.exception.NotHostOfBoardExcepion;
import com.yogit.server.board.exception.boardCategory.NotFoundCategoryException;
import com.yogit.server.board.exception.boardimage.NotFoundBoardImageException;
import com.yogit.server.board.repository.BoardImageRepository;
import com.yogit.server.board.repository.BoardUserRepository;
import com.yogit.server.board.repository.CategoryRepository;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.s3.AwsS3Service;
import com.yogit.server.user.entity.City;
import com.yogit.server.user.entity.CityName;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.exception.city.NotFoundCityException;
import com.yogit.server.user.repository.CityRepository;
import com.yogit.server.user.repository.UserRepository;
import com.yogit.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CategoryRepository categoryRepository;
    private final AwsS3Service awsS3Service;
    private final BoardImageRepository boardImageRepository;
    private final BlockRepository blockRepository;
    private final UserService userService;
    private final BoardUserRepository boardUserRepository;

    private static final int PAGING_SIZE = 10;
    private static final String PAGING_STANDARD = "date";

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardRes> createBoard(CreateBoardReq dto){

        userService.validateRefreshToken(dto.getHostId(), dto.getRefreshToken());

        // host ??????
        User host = userRepository.findByUserId(dto.getHostId())
                .orElseThrow(() -> new NotFoundUserException());
        // city??????
        // ????????? ???????????? city??? ??????
        City city = null;
        if(cityRepository.existsByCityName(dto.getCityName())){
            city = cityRepository.findByCityName(dto.getCityName());
        }
        else{ // ????????? ???????????? ?????? city??? ??????
            city = City.builder()
                    .cityName(dto.getCityName())
                    .build();
            cityRepository.save(city);
        }

        // category ??????
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundCategoryException());

        // board ?????? ??????
        Board board = new Board(dto, host, city, category);
        board.changeBoardCurrentMember(0);// currentMember ?????????=0

        // ????????? boardUser ?????? ??? board??? ??????
        board.addBoardUser(new BoardUser(host, board));

        // board ??????
        Board savedBoard = boardRepository.save(board);

        //BoardImages aws s3??? ?????? ??? ????????????????????? ??????
        List<String> imageUrls = new ArrayList<>();
        if(dto.getImages() != null){
            List<String> imageUUids = awsS3Service.uploadImages(dto.getImages());
            for(String i : imageUUids){
                BoardImage boardImage = new BoardImage(savedBoard, i);
                boardImageRepository.save(boardImage);
//                boardRes.addImageUrl(awsS3Service.makeUrlOfFilename(i)); // res??? ??????
                imageUrls.add(awsS3Service.makeUrlOfFilename(i));
            }
        }

        BoardRes boardRes = BoardRes.toDto(savedBoard, awsS3Service.makeUrlsOfFilenames(board.getBoardImagesUUids()), awsS3Service.makeUrlOfFilename(host.getProfileImg())); // resDto ??????
        return ApplicationResponse.create("????????? ?????????????????????.", boardRes);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardRes> updateBoard(PatchBoardReq dto){
        userService.validateRefreshToken(dto.getHostId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getHostId())
                .orElseThrow(() -> new NotFoundUserException());

        // ????????? ???????????? city??? ??????
        City city = null;
        if(cityRepository.existsByCityName(dto.getCityName())){
            city = cityRepository.findByCityName(dto.getCityName());
        }
        else{ // ????????? ???????????? ?????? city??? ??????
            city = City.builder()
                    .cityName(dto.getCityName())
                    .build();
            cityRepository.save(city);
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundCategoryException());

        //validation: board ?????? ??????
        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());
        //validation: ???????????? host ??????
        if(!board.getHost().equals(user)){
            throw new NotHostOfBoardExcepion();
        }

        board.updateBoard(dto, city, category);

        //BoardImages aws s3??? ?????? ??? ????????????????????? ??????
        if(dto.getImages() != null){
            List<String> imageUUids = awsS3Service.uploadImages(dto.getImages());
            for(String i : imageUUids){
                BoardImage boardImage = new BoardImage(board, i);
                boardImageRepository.save(boardImage);
            }
        }

        // ????????? ????????? ??????
        if(dto.getDeleteImageIds() != null){
            for(Long id: dto.getDeleteImageIds()){
                this.deleteBoardImage(new DeleteBoardImageReq(board.getId(), user.getId(), id, user.getRefreshToken()));
            }
        }

        BoardRes boardRes = BoardRes.toDto(board, awsS3Service.makeUrlsOfFilenames(board.getBoardImagesUUids()), awsS3Service.makeUrlOfFilename(user.getProfileImg()));
        return ApplicationResponse.ok(boardRes);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardRes> deleteBoard(DeleteBoardReq dto){

        userService.validateRefreshToken(dto.getHostId(), dto.getRefreshToken());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        User user = userRepository.findByUserId(dto.getHostId())
                .orElseThrow(() -> new NotFoundUserException());
        //validation: ???????????? host ??????
        if (!board.getHost().equals(user)) {
            throw new NotHostOfBoardExcepion();
        }

        board.deleteBoard();
        BoardRes boardRes = BoardRes.toDto(board, awsS3Service.makeUrlsOfFilenames(board.getBoardImagesUUids()), awsS3Service.makeUrlOfFilename(user.getProfileImg()));
        return ApplicationResponse.ok(boardRes);
    }


    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<List<List<GetAllBoardRes>>> findAllBoards(GetAllBoardsReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        int cursor = dto.getCursor();

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        List<Category> categoryList = categoryRepository.findAllCategories();

        // jpa ?????? ?????? order
        Sort sort = Sort.by(
                Sort.Order.desc("currentMember"),
                Sort.Order.asc("date")
        );
        PageRequest pageRequest = PageRequest.of(cursor, 10, sort); // ????????? ?????? ??????

        // ????????? ?????? ??????????????? ??? board category ??? ?????? ????????? ?????? (10??????)
        List<List<GetAllBoardRes>> res = new ArrayList<>();
        for(int i=0;i< categoryList.size();i++){
            Slice<Board> boards = boardRepository.findAllBoardsByCategory(pageRequest, categoryList.get(i).getId());
            //  ?????? res??? ?????????uuid -> aws s3 url??? ??????
            List<GetAllBoardRes> boardsRes = boards.stream()
                    .map(board -> GetAllBoardRes.toDto(board, awsS3Service.makeUrlOfFilename(board.getBoardImagesUUids().get(0)), awsS3Service.makeUrlOfFilename(user.getProfileImg())))
                    .collect(Collectors.toList());
            // ?????? ???????????? ???????????? ??? ????????? ??????
            res.add(boardsRes);
        }

        return ApplicationResponse.ok(res);
    }


    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<List<GetAllBoardRes>> findMyClubBoards(GetMyClubBoardsReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        int cursor = dto.getCursor();

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        // jpa ?????? ?????? order
        Sort sort = Sort.by(
                Sort.Order.desc("currentMember"),
                Sort.Order.asc("date")
        );
        PageRequest pageRequest = PageRequest.of(cursor, PAGING_SIZE, sort);

        Slice<Board> boards = null;
        Slice<BoardUser> boardUsers = null;
        List<GetAllBoardRes> res = null;
        /*
        1.????????? ??????: Opened Club
        2.????????? ?????? : Applied Club
         */
        if(dto.getMyClubType().equals(MyClubType.OPENED_CLUB.toString())){
            boards = boardRepository.findMyClubBoardsByUserId(pageRequest, dto.getUserId());

            //  ?????? res??? ?????????uuid -> aws s3 url??? ??????
            if(boards != null){
                res = boards.stream()
                        .map(board -> GetAllBoardRes.toDto(board, awsS3Service.makeUrlOfFilename(board.getBoardImagesUUids().get(0)), awsS3Service.makeUrlOfFilename(user.getProfileImg())))
                        .collect(Collectors.toList());
            }
        }
        else if(dto.getMyClubType().equals(MyClubType.APPLIED_CLUB.toString())){
            boardUsers = boardUserRepository.findByUserId(dto.getUserId());
//            System.out.println("?????? ??????????: "+ boardUsers);

            //  ?????? res??? ?????????uuid -> aws s3 url??? ??????
            if(boardUsers!= null && !boardUsers.isEmpty()){
                res = boardUsers.stream()
                        .filter(boardUser -> !boardUser.getBoard().getHost().getId().equals(dto.getUserId())) // ??????1: ???????????? ?????? ???
                        .map(boardUser -> GetAllBoardRes.toDto(boardUser.getBoard(), awsS3Service.makeUrlOfFilename(boardUser.getBoard().getBoardImagesUUids().get(0)), awsS3Service.makeUrlOfFilename(user.getProfileImg())))
                        .collect(Collectors.toList());
            }
        }
        else{ throw new InvalidMyClubTypeException();}

        return ApplicationResponse.ok(res);
    }


    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<List<GetAllBoardRes>> findAllBoardsByCategory(GetAllBoardsByCategoryReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        int cursor = dto.getCursor();

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        // jpa ?????? ?????? order
        Sort sort = Sort.by(
                Sort.Order.desc("currentMember"),
                Sort.Order.asc("date")
        );
        PageRequest pageRequest = PageRequest.of(cursor, PAGING_SIZE, sort);

        Slice<Board> boards = boardRepository.findAllBoardsByCategory(pageRequest, dto.getCategoryId());
        //  ?????? res??? ?????????uuid -> aws s3 url??? ??????
        List<GetAllBoardRes> boardsRes = boards.stream()
                .map(board -> GetAllBoardRes.toDto(board, awsS3Service.makeUrlOfFilename(board.getBoardImagesUUids().get(0)), awsS3Service.makeUrlOfFilename(user.getProfileImg())))
                .collect(Collectors.toList());
        return ApplicationResponse.ok(boardsRes);
    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<List<List<GetAllBoardRes>>> findBoardsByCategories(GetBoardsByCategoriesReq dto) {

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        int cursor = dto.getCursor();
        List<List<GetAllBoardRes>> boardsByCategories = new ArrayList<>();

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        List<Category> categories = categoryRepository.findAllCategories();
        if(categories.isEmpty()){
            System.out.println("??????????????? ????????????.");
        }

        List<User> blockedUsers = blockRepository.findBlocksByBlockingUserId(dto.getUserId()).stream()
                .map(block -> block.getBlockedUser())
                .collect(Collectors.toList());

        // jpa ?????? ?????? order
        Sort sort = Sort.by(
                Sort.Order.desc("currentMember"),
                Sort.Order.asc("date")
        );
        PageRequest pageRequest = PageRequest.of(cursor, PAGING_SIZE, sort);

        // ???????????? ??? ????????? ????????? ??????
        for(Category category: categories){
            Slice<Board> boards = boardRepository.findAllBoardsByCategory(pageRequest, category.getId());
            //  ?????? res??? ?????????uuid -> aws s3 url??? ??????
            List<GetAllBoardRes> boardsRes = boards.stream()
                    .filter(board -> !blockedUsers.contains(board.getHost()))// ???????????? ????????? ????????? ??????
                    .map(board -> GetAllBoardRes.toDto(board, awsS3Service.makeUrlOfFilename(board.getBoardImagesUUids().get(0)), awsS3Service.makeUrlOfFilename(board.getHost().getProfileImg())))
                    .collect(Collectors.toList());

            boardsByCategories.add(boardsRes);
        }

        return ApplicationResponse.ok(boardsByCategories);
    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<GetBoardRes> findBoard(GetBoardReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        List<User> participants = board.getBoardUsers().stream()
                .filter(boardUser -> !boardUser.getUser().equals(board.getHost()))
                .map(boardUser -> boardUser.getUser())
                .collect(Collectors.toList());

        List<String> participantsImageUUIds = board.getBoardUsers().stream()
                .filter(boardUser -> !boardUser.getUser().equals(board.getHost()))
                .map(boardUser -> boardUser.getUser().getProfileImg())
                .collect(Collectors.toList());

        GetBoardRes res = GetBoardRes.toDto(board, awsS3Service.makeUrlsOfFilenames(board.getBoardImagesUUids()), awsS3Service.makeUrlOfFilename(board.getHost().getProfileImg()), participants, awsS3Service.makeUrlsOfFilenames(participantsImageUUIds));
        return ApplicationResponse.ok(res);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<DeleteBoardImageRes> deleteBoardImage(DeleteBoardImageReq dto){

        userService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        //validation: ???????????? host ??????
        if (!board.getHost().equals(user)) {
            throw new NotHostOfBoardExcepion();
        }

        BoardImage boardImage = boardImageRepository.findBoardImageById(dto.getBoardImageId())
                .orElseThrow(() -> new NotFoundBoardImageException());
        String imgUrl = awsS3Service.makeUrlOfFilename(boardImage.getImgUUid());

        boardImage.deleteBoardImage();
        DeleteBoardImageRes res = DeleteBoardImageRes.toDto(boardImage, imgUrl);
        return ApplicationResponse.ok(res);
    }

}
