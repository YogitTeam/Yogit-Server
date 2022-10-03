package com.yogit.server.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "language_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private LanguageName name;

    @Enumerated(EnumType.STRING)
    private LanguageLevel level;

    @Builder
    public Language(User user, LanguageName name, LanguageLevel level){
        this.name = name;
        this.level = level;
        user.addLanguage(this);
    }
}
