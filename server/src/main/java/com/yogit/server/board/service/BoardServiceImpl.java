package com.yogit.server.board.service;

import com.yogit.server.block.repository.BlockRepository;
import com.yogit.server.board.dto.request.*;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageReq;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageRes;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.board.dto.response.GetAllBoardRes;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.BoardImage;
import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.board.entity.Category;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.exception.NotHostOfBoardExcepion;
import com.yogit.server.board.exception.boardCategory.NotFoundCategoryException;
import com.yogit.server.board.exception.boardimage.NotFoundBoardImageException;
import com.yogit.server.board.repository.BoardImageRepository;
import com.yogit.server.board.repository.CategoryRepository;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.s3.AwsS3Service;
import com.yogit.server.user.entity.City;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.exception.city.NotFoundCityException;
import com.yogit.server.user.repository.CityRepository;
import com.yogit.server.user.repository.UserRepository;
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

    private static final int PAGING_SIZE = 10;
    private static final String PAGING_STANDARD = "date";

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardRes> createBoard(CreateBoardReq dto){

        // host 조회
        User host = userRepository.findById(dto.getHostId())
                .orElseThrow(() -> new NotFoundUserException());
        // city조회
        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new NotFoundCityException());
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

        BoardRes boardRes = BoardRes.toDto(savedBoard, imageUrls, awsS3Service.makeUrlOfFilename(host.getProfileImg())); // resDto 벼환
        return ApplicationResponse.create("요청에 성공하였습니다.", boardRes);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardRes> updateBoard(PatchBoardReq dto){
        List<String> imageUrls = new ArrayList<>();

        User user = userRepository.findById(dto.getHostId())
                .orElseThrow(() -> new NotFoundUserException());

        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new NotFoundCityException());

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
        if(!dto.getImages().isEmpty()){
            List<String> imageUUids = awsS3Service.uploadImages(dto.getImages());
            for(String i : imageUUids){
                BoardImage boardImage = new BoardImage(board, i);
                boardImageRepository.save(boardImage);
                imageUrls.add(awsS3Service.makeUrlOfFilename(i));
            }
        }
        BoardRes boardRes = BoardRes.toDto(board, imageUrls, awsS3Service.makeUrlOfFilename(user.getProfileImg()));
        return ApplicationResponse.ok(boardRes);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardRes> deleteBoard(DeleteBoardReq dto){

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        User user = userRepository.findById(dto.getHostId())
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
        int cursor = dto.getCursor();

        User user = userRepository.findById(dto.getUserId())
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
    public ApplicationResponse<List<GetAllBoardRes>> findAllBoardsByCategory(GetAllBoardsByCategoryReq dto){
        int cursor = dto.getCursor();

        User user = userRepository.findById(dto.getUserId())
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
    public ApplicationResponse<List<List<GetAllBoardRes>>> findBoardsByCategories(GetBoardsByCategories dto) {
        int cursor = dto.getCursor();
        List<List<GetAllBoardRes>> boardsByCategories = new ArrayList<>();

        User user = userRepository.findById(dto.getUserId())
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
                    .map(board -> GetAllBoardRes.toDto(board, awsS3Service.makeUrlOfFilename(board.getBoardImagesUUids().get(0)), awsS3Service.makeUrlOfFilename(user.getProfileImg())))
                    .collect(Collectors.toList());

            boardsByCategories.add(boardsRes);
        }

        return ApplicationResponse.ok(boardsByCategories);
    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<BoardRes> findBoard(GetBoardReq dto){
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        BoardRes boardRes = BoardRes.toDto(board, awsS3Service.makeUrlsOfFilenames(board.getBoardImagesUUids()), awsS3Service.makeUrlOfFilename(user.getProfileImg()));
        return ApplicationResponse.ok(boardRes);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<DeleteBoardImageRes> deleteBoardImage(DeleteBoardImageReq dto){
        User user = userRepository.findById(dto.getUserId())
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
