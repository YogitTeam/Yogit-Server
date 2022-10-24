package com.yogit.server.board.entity;

import com.yogit.server.board.dto.request.clipboard.CreateClipBoardReq;
import com.yogit.server.config.domain.BaseEntity;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClipBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clip_board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "clipBoard")
    private List<Comment> comments;

    private String title;

    private String content;

    public ClipBoard(CreateClipBoardReq dto, User user, Board board) {
        this.user = user;
        this.board = board;
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }
}
