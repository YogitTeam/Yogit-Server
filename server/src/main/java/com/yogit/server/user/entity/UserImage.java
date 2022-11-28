package com.yogit.server.user.entity;

import com.yogit.server.config.domain.BaseEntity;
import com.yogit.server.config.domain.BaseStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String imgUUid;

    @Builder
    public UserImage(User user, String imgUUid){
        this.user = user;
        user.addImage(this);

        this.imgUUid = imgUUid;
    }

    public void deleteUserImage(){
        this.setStatus(BaseStatus.INACTIVE);
    }
}
