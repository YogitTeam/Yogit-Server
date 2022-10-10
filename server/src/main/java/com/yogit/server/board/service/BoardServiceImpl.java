package com.yogit.server.board.service;

import com.yogit.server.board.dto.request.CreateBoardReq;
import com.yogit.server.board.dto.request.DeleteBoardReq;
import com.yogit.server.board.dto.request.PatchBoardReq;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.board.entity.Category;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.exception.NotHostOfBoardExcepion;
import com.yogit.server.board.exception.boardCategory.NotFoundCategoryException;
import com.yogit.server.board.repository.CategoryRepository;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.entity.City;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.exception.city.NotFoundCityException;
import com.yogit.server.user.repository.CityRepository;
import com.yogit.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.sound.midi.Patch;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CategoryRepository categoryRepository;

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

        //TODO: BoardImages aws s3에 저장 , url board엔티티에 추가

        // board 저장
        Board savedBoard = boardRepository.save(board);
        BoardRes boardRes = BoardRes.toDto(savedBoard); // resDto 벼환
        return ApplicationResponse.create("요청에 성공하였습니다.", boardRes);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardRes> updateBoard(PatchBoardReq dto){

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
        BoardRes boardRes = BoardRes.toDto(board);
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
        BoardRes boardRes = BoardRes.toDto(board);
        return ApplicationResponse.ok(boardRes);
    }

}
