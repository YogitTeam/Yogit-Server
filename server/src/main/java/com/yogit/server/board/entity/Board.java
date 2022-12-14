package com.yogit.server.board.entity;

import com.yogit.server.board.dto.request.CreateBoardReq;
import com.yogit.server.board.dto.request.PatchBoardReq;
import com.yogit.server.config.domain.BaseEntity;
import com.yogit.server.config.domain.BaseStatus;
import com.yogit.server.report.entity.BoardReport;
import com.yogit.server.user.entity.City;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User host;

    private String title;

    private String address;

    private String addressDetail; // 상세 주소

    private Float longitute;

    private Float latitude;

    private LocalDateTime date; // 모임 시각

    private String notice;

    private String introduction; // 게시글 내용 상세설명

    private String kindOfPerson; // 이런 사람을 원합니다 설명 글.

    private Integer currentMember;

    private Integer totalMember;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL) // 보드 생성 순간 보드 유저 리스트 생성
    private List<BoardUser> boardUsers = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardImage> boardImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "board")
    private List<BookMark> bookMarks;

    @OneToMany(mappedBy = "board")
    private List<ClipBoard> clipBoards;

    @OneToMany(mappedBy = "board")
    private List<BoardReport> boardReports;

    private Integer reportedCnt;

    // 생성자
    @Builder
    public Board(Long id, City city, User host, String title, String address, String addressDetail, float longitute, float latitude, LocalDateTime date, String notice, String introduction, String kindOfPerson, int currentMember, int totalMember, List<BoardUser> boardUsers, List<BoardImage> boardImages, Category category, List<BookMark> bookMarks, List<ClipBoard> clipBoards) {
        this.id = id;
        this.city = city;
        this.host = host;
        this.title = title;
        this.address = address;
        this.addressDetail = addressDetail;
        this.longitute = longitute;
        this.latitude = latitude;
        this.date = date;
        this.notice = notice;
        this.introduction = introduction;
        this.kindOfPerson = kindOfPerson;
        this.currentMember = currentMember;
        this.totalMember = totalMember;
        this.boardUsers = boardUsers;
        this.boardImages = boardImages;
        this.category = category;
        this.bookMarks = bookMarks;
        this.clipBoards = clipBoards;
    }

    // 개발 중 임시 생성자
    public Board(CreateBoardReq dto, User host, City city, Category category){
//        this.id = id;
        this.city = city;
        this.host = host;
        this.title = dto.getTitle();
        this.address = dto.getAddress();
        this.addressDetail = dto.getAddressDetail();
        this.longitute = dto.getLongitute();
        this.latitude = dto.getLatitude();
        this.date = dto.getDate();
        this.notice = dto.getNotice();
        this.introduction = dto.getIntroduction();
        this.kindOfPerson = dto.getKindOfPerson();
//        this.currentMember = currentMember;
        this.totalMember = dto.getTotalMember();
//        this.boardUsers = boardUsers;
//        this.boardImages = boardImages;
        this.category = category;
//        this.bookMarks = bookMarks;
//        this.clipBoards = clipBoards;
        this.reportedCnt = 0;
    }

    /*
    연관관계 편의 메서드
     */

    public void changeBoardCurrentMember(int currentMember){
        this.currentMember = currentMember;
    }

    public void addBoardUser(BoardUser boardUser){
        this.boardUsers.add(boardUser);
    }

    public void addBoardImage(BoardImage boardImage){
        this.boardImages.add(boardImage);
    }

    public void updateBoard(PatchBoardReq dto, City city, Category category){
        this.city = city;
        this.title = dto.getTitle();
        this.address = dto.getAddress();
        this.addressDetail = dto.getAddressDetail();
        this.longitute = dto.getLongitute();
        this.latitude = dto.getLatitude();
        this.date = dto.getDate();
        this.notice = dto.getNotice();
        this.introduction = dto.getIntroduction();
        this.kindOfPerson = dto.getKindOfPerson();
        this.totalMember = dto.getTotalMember();
//        this.boardImages = boardImages;
        this.category = category;
    }

    public void deleteBoard(){
        this.setStatus(BaseStatus.INACTIVE);
    }

    // BoardImage리스트의 UUID리스트만 String으로 반환
    public List<String> getBoardImagesUUids(){
        return this.boardImages.stream()
                .filter(boardImage -> boardImage.getStatus().equals(BaseStatus.ACTIVE))
                .map(boardImage -> boardImage.getImgUUid())
                .collect(Collectors.toList());
    }

    public void addCurrentMember(){
        this.currentMember+=1;
    }

    public void changeReportedCnt(){
        this.reportedCnt+=1;
    }

    public void addCity(City city){
        this.city = city;
    }
}
