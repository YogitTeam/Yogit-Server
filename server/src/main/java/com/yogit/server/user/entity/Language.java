package com.yogit.server.user.entity;

import com.yogit.server.config.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Language extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String code;
    private Integer level;

    @Builder
    public Language (User user, String code, Integer level){
        this.code = code;
        this.level = level;

        this.user = user;
        user.addLanguage(this);
    }
}
