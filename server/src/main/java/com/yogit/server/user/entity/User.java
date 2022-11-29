package com.yogit.server.user.entity;

import com.yogit.server.block.entity.Block;
import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.board.entity.BookMark;
import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.entity.Comment;
import com.yogit.server.config.domain.BaseEntity;
import com.yogit.server.report.entity.UserReport;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="city_id")
    private City city;

    @OneToMany(mappedBy = "user")
    private List<BoardUser> boardUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserInterest> userInterests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Language> languages = new ArrayList<>();

    private String loginId;
    private String name;
    private String profileImg;

    private String aboutMe; // 300자 이내

    // location
    private Float longtitude;
    private Float latitude;
    private String administrativeArea;

    private String job;
    private Integer age;
    private Float memberTemp;
    private String phoneNum;
    private String gender;
    private String nationality;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user")
    private List<BookMark> bookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ClipBoard> clipBoard = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserImage> userImages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "reportingUser")
    private List<UserReport> reportingUsers = new ArrayList<>();

    @OneToMany(mappedBy = "reportedUser")
    private List<UserReport> reportedUsers = new ArrayList<>();

    private Integer reportingCnt;
    private Integer reportedCnt;

    @OneToMany(mappedBy = "blockingUser")
    private List<Block> blockingUsers = new ArrayList<>();

    @OneToMany(mappedBy = "blockedUser")
    private List<Block> blockedUsers = new ArrayList<>();

    @Builder
    public User (String loginId, String phoneNum){
        this.loginId = loginId;
        this.phoneNum = phoneNum;
    }

    public User (String loginId, String refreshToken, String name){
        this.loginId = loginId;
        this.refreshToken = refreshToken;
        this.name = name;
        this.reportingCnt=0;
        this.reportedCnt=0;
    }

    public User (String loginId, String refreshToken, String name, UserType userType){
        this.loginId = loginId;
        this.refreshToken = refreshToken;
        this.name = name;
        this.userType = userType;
        this.reportingCnt=0;
        this.reportedCnt=0;
    }

    public void addLanguage(Language language){
        this.languages.add(language);
    }

    public void changeUserInfo(String userName, Integer userAge, String gender, String nationality){
        if(userName != null) this.name = userName;
        if(userAge != 0) this.age = userAge;
        if(gender != null) this.gender = gender;
        if(nationality != null) this.nationality = nationality;
    }

    public void delUser(){
        this.name = null;
        this.profileImg = null;
        this.userStatus = UserStatus.DELETE;
    }

    public void addImage(UserImage userImage) {
        this.userImages.add(userImage);
    }

    public void changeMainImgUUid(String mainImgUUid){
        this.profileImg = mainImgUUid;
    }

    public void addUserInterest(UserInterest userInterest){
        this.userInterests.add(userInterest);
    }

    public void addAdditionalProfile(float latitude, float longitude, String aboutMe, String administrativeArea, String job){
        this.latitude = latitude;
        this.longtitude = longitude;
        this.aboutMe = aboutMe;
        this.administrativeArea = administrativeArea;
        this.job = job;
    }

    public void addCity(City city){
        this.city = city;
    }

    public void addPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
    }

    public void changeReportingCnt(){
        this.reportingCnt+=1;
    }

    public void changeReportedCnt(){
        this.reportedCnt+=1;
    }
}
