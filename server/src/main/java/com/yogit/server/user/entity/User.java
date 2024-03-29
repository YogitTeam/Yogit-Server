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

    private String job;
    private Integer age;
    private Float memberTemp;
    private String phoneNum;
    private String gender;
    private String nationality;

    private String refreshToken;
    private String access_token;
    private Long expires_in;

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

    private String deviceToken;

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

    public User (String loginId, String refreshToken, String name, UserType userType, String access_token, Long expires_in){
        this.loginId = loginId;
        this.refreshToken = refreshToken;
        this.name = name;
        this.userType = userType;
        this.reportingCnt=0;
        this.reportedCnt=0;
        this.access_token = access_token;
        this.expires_in = expires_in;
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

    public void addImage(UserImage userImage) {
        this.userImages.add(userImage);
    }

    public void changeMainImgUUid(String mainImgUUid){
        this.profileImg = mainImgUUid;
    }

    public void addUserInterest(UserInterest userInterest){
        this.userInterests.add(userInterest);
    }

    public void addAdditionalProfile(float latitude, float longitude, String aboutMe, String job){
        if(latitude != 0) this.latitude = latitude;
        if(longitude != 0) this.longtitude = longitude;
        if(aboutMe != null) this.aboutMe = aboutMe;
        if(job != null) this.job = job;
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

    public void addDeviceToken(String deviceToken){
        this.deviceToken = deviceToken;
    }

    public void changeUserStatus(UserStatus userStatus){
        this.userStatus = userStatus;
    }

    public void deleteUser(){
        this.loginId = null;
        this.name = null;
        this.profileImg = null;
        this.aboutMe = null;
        this.longtitude = null;
        this.latitude = null;
        this.job = null;
        this.age = null;
        this.memberTemp = null;
        this.phoneNum = null;
        this.gender = null;
        this.nationality = null;
        this.refreshToken = null;
        this.reportingCnt = null;
        this.reportedCnt = null;
        this.deviceToken = null;
        this.access_token = null;
        this.expires_in = null;

        this.userStatus = UserStatus.DELETE;
    }
}
