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

        // host 조회
        User host = userRepository.findByUserId(dto.getHostId())
                .orElseThrow(() -> new NotFoundUserException());
        // city조회
        // 기존에 존재하는 city인 경우
        City city = null;
        if(cityRepository.existsByCityName(dto.getCityName())){
            city = cityRepository.findByCityName(dto.getCityName());
        }
        else{ // 기존에 존재하지 않는 city인 경우
            city = City.builder()
                    .cityName(dto.getCityName())
                    .build();
            cityRepository.save(city);
        }

        // category 조회
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundCategoryException());

        // board 객체 생성
        Board board = new Board(dto, host, city, category);
        board.changeBoardCurrentMember(0);// currentMember 디폴트=0

        // 호스트 boardUser 생성 및 board에 추가
        board.addBoardUser(new BoardUser(host, board));

        // board 저장
        Board savedBoard = boardRepository.save(board);

        //BoardImages aws s3에 저장 후 리파지토리에도 저장
        List<String> imageUrls = new ArrayList<>();
        if(dto.getImages() != null){
            List<String> imageUUids = awsS3Service.uploadImages(dto.getImages());
            for(String i : imageUUids){
                BoardImage boardImage = new BoardImage(savedBoard, i);
                boardImageRepository.save(boardImage);
//                boardRes.addImageUrl(awsS3Service.makeUrlOfFilename(i)); // res에 추가
                imageUrls.add(awsS3Service.makeUrlOfFilename(i));
            }
        }

        BoardRes boardRes = BoardRes.toDto(savedBoard, awsS3Service.makeUrlsOfFilenames(board.getBoardImagesUUids()), awsS3Service.makeUrlOfFilename(host.getProfileImg())); // resDto 벼환
        return ApplicationResponse.create("요청에 성공하였습니다.", boardRes);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardRes> updateBoard(PatchBoardReq dto){
        userService.validateRefreshToken(dto.getHostId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getHostId())
                .orElseThrow(() -> new NotFoundUserException());

        // 기존에 존재하는 city인 경우
        City city = null;
        if(cityRepository.existsByCityName(dto.getCityName())){
            city = cityRepository.findByCityName(dto.getCityName());
        }
        else{ // 기존에 존재하지 않는 city인 경우
            city = City.builder()
                    .cityName(dto.getCityName())
                    .build();
            cityRepository.save(city);
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundCategoryException());

        //validation: board 존재 여부
        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());
        //validation: 요청자와 host 비교
        if(!board.getHost().equals(user)){
            throw new NotHostOfBoardExcepion();
        }

        board.updateBoard(dto, city, category);

        //BoardImages aws s3에 저장 후 리파지토리에도 저장
        if(dto.getImages() != null){
            List<String> imageUUids = awsS3Service.uploadImages(dto.getImages());
            for(String i : imageUUids){
                BoardImage boardImage = new BoardImage(board, i);
                boardImageRepository.save(boardImage);
            }
        }

        // 게시글 이미지 삭제
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
        //validation: 요청자와 host 비교
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

        // jpa 다중 정렬 order
        Sort sort = Sort.by(
                Sort.Order.desc("currentMember"),
                Sort.Order.asc("date")
        );
        PageRequest pageRequest = PageRequest.of(cursor, 10, sort); // 페이징 요청 객체

        // 사이즈 만큼 반복하면서 각 board category 별 보드 리스트 조회 (10개씩)
        List<List<GetAllBoardRes>> res = new ArrayList<>();
        for(int i=0;i< categoryList.size();i++){
            Slice<Board> boards = boardRepository.findAllBoardsByCategory(pageRequest, categoryList.get(i).getId());
            //  보드 res에 이미지uuid -> aws s3 url로 변환
            List<GetAllBoardRes> boardsRes = boards.stream()
                    .map(board -> GetAllBoardRes.toDto(board, awsS3Service.makeUrlOfFilename(board.getBoardImagesUUids().get(0)), awsS3Service.makeUrlOfFilename(user.getProfileImg())))
                    .collect(Collectors.toList());
            // 전체 리스트에 카테고리 별 리스트 추가
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

        // jpa 다중 정렬 order
        Sort sort = Sort.by(
                Sort.Order.desc("currentMember"),
                Sort.Order.asc("date")
        );
        PageRequest pageRequest = PageRequest.of(cursor, PAGING_SIZE, sort);

        Slice<Board> boards = null;
        Slice<BoardUser> boardUsers = null;
        List<GetAllBoardRes> res = null;
        /*
        1.생성한 보드: Opened Club
        2.참여한 보드 : Applied Club
         */
        if(dto.getMyClubType().equals(MyClubType.OPENED_CLUB.toString())){
            boards = boardRepository.findMyClubBoardsByUserId(pageRequest, dto.getUserId());

            //  보드 res에 이미지uuid -> aws s3 url로 변환
            if(boards != null){
                res = boards.stream()
                        .map(board -> GetAllBoardRes.toDto(board, awsS3Service.makeUrlOfFilename(board.getBoardImagesUUids().get(0)), awsS3Service.makeUrlOfFilename(user.getProfileImg())))
                        .collect(Collectors.toList());
            }
        }
        else if(dto.getMyClubType().equals(MyClubType.APPLIED_CLUB.toString())){
            boardUsers = boardUserRepository.findByUserId(dto.getUserId());
//            System.out.println("보드 유저는?: "+ boardUsers);

            //  보드 res에 이미지uuid -> aws s3 url로 변환
            if(boardUsers!= null && !boardUsers.isEmpty()){
                res = boardUsers.stream()
                        .filter(boardUser -> !boardUser.getBoard().getHost().getId().equals(dto.getUserId())) // 조건1: 호스트가 아닌 것
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

        // jpa 다중 정렬 order
        Sort sort = Sort.by(
                Sort.Order.desc("currentMember"),
                Sort.Order.asc("date")
        );
        PageRequest pageRequest = PageRequest.of(cursor, PAGING_SIZE, sort);

        Slice<Board> boards = boardRepository.findAllBoardsByCategory(pageRequest, dto.getCategoryId());
        //  보드 res에 이미지uuid -> aws s3 url로 변환
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
            System.out.println("카테고리가 없습니다.");
        }

        List<User> blockedUsers = blockRepository.findBlocksByBlockingUserId(dto.getUserId()).stream()
                .map(block -> block.getBlockedUser())
                .collect(Collectors.toList());

        // jpa 다중 정렬 order
        Sort sort = Sort.by(
                Sort.Order.desc("currentMember"),
                Sort.Order.asc("date")
        );
        PageRequest pageRequest = PageRequest.of(cursor, PAGING_SIZE, sort);

        // 카테고리 별 리스트 반복문 조회
        for(Category category: categories){
            Slice<Board> boards = boardRepository.findAllBoardsByCategory(pageRequest, category.getId());
            //  보드 res에 이미지uuid -> aws s3 url로 변환
            List<GetAllBoardRes> boardsRes = boards.stream()
                    .filter(board -> !blockedUsers.contains(board.getHost()))// 차단당한 유저의 데이터 제외
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

        //validation: 요청자와 host 비교
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
