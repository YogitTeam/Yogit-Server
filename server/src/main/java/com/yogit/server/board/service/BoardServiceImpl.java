package com.yogit.server.board.service;

import com.yogit.server.board.dto.request.*;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.BoardImage;
import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.board.entity.Category;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.exception.NotHostOfBoardExcepion;
import com.yogit.server.board.exception.boardCategory.NotFoundCategoryException;
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

        BoardRes boardRes = BoardRes.toDto(savedBoard, imageUrls); // resDto 벼환
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
        BoardRes boardRes = BoardRes.toDto(board, imageUrls);
        return ApplicationResponse.ok(boardRes);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardRes> deleteBoard(DeleteBoardReq dto){
        List<String> imageUrls = new ArrayList<>();

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        User user = userRepository.findById(dto.getHostId())
                .orElseThrow(() -> new NotFoundUserException());
        //validation: 요청자와 host 비교
        if (!board.getHost().equals(user)) {
            throw new NotHostOfBoardExcepion();
        }

        board.deleteBoard();
        BoardRes boardRes = BoardRes.toDto(board, imageUrls);
        return ApplicationResponse.ok(boardRes);
    }


    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<List<BoardRes>> findAllBoards(GetAllBoardsReq dto){
        int cursor = dto.getCursor();

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        PageRequest pageRequest = PageRequest.of(cursor, PAGING_SIZE, Sort.by(Sort.Direction.ASC, PAGING_STANDARD ));

        Slice<Board> boards = boardRepository.findAllBoards(pageRequest);
        List<BoardRes> boardsRes = boards.stream()
                .map(board -> BoardRes.toDto(board, awsS3Service.makeUrlsOfFilenames(board.getBoardImagesUUids())))
                .collect(Collectors.toList());
        return ApplicationResponse.ok(boardsRes);
    }


    @Transactional(readOnly = true)
    @Override
    public ApplicationResponse<BoardRes> findBoard(GetBoardReq dto){
        List<String> imageUrls = new ArrayList<>();

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        BoardRes boardRes = BoardRes.toDto(board, imageUrls);
        return ApplicationResponse.ok(boardRes);
    }

}
