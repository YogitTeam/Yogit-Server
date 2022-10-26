package com.yogit.server.user.entity;

import com.yogit.server.board.entity.*;
import com.yogit.server.config.domain.BaseEntity;
import com.yogit.server.user.dto.request.EditUserEssentialProfileReq;
import lombok.*;

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
    private float longtitude;
    private float latitude;
    private String administrativeArea;

    private Integer age;
    private float memberTemp;
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Nationality nationality;

    @OneToMany(mappedBy = "user")
    private List<BookMark> bookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ClipBoard> clipBoard = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserImage> userImages = new ArrayList<>();

    @Builder
    public User (String name, int age, Gender gender, Nationality nationality){
        this.name = name;
        this.age = age;
        this.memberTemp = 0;
        this.gender = gender;
        this.nationality = nationality;
        this.userStatus = UserStatus.ACTIVE;
    }

    public void addLanguage(Language language){
        this.languages.add(language);
    }

    public void changeUserInfo(EditUserEssentialProfileReq editUserEssentialProfileReq){
        if(editUserEssentialProfileReq.getUserName() != null) this.name = editUserEssentialProfileReq.getUserName();
        if(editUserEssentialProfileReq.getUserAge() != null) this.age = editUserEssentialProfileReq.getUserAge();
        if(editUserEssentialProfileReq.getGender() != null) this.gender = editUserEssentialProfileReq.getGender();
        if(editUserEssentialProfileReq.getNationality() != null) this.nationality = editUserEssentialProfileReq.getNationality();
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

    public void addAdditionalProfile(float latitude, float longitude, String aboutMe){
        this.latitude = latitude;
        this.longtitude = longitude;
        this.aboutMe = aboutMe;
    }

    public void addCity(City city){
        this.city = city;
    }
}
