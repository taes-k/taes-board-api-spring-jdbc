package com.taes.board.api.domain.post.entity;

import com.taes.board.api.domain.board.entity.Board;
import com.taes.board.api.domain.comment.entity.Comment;
import com.taes.board.api.domain.user.entity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET deleted=true WHERE seq=?")
@Where(clause = "deleted = false")
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @Column(length = 300, nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_seq", insertable=false, updatable=false)
    private Board board;

    @Column(name = "board_seq", nullable = false)
    private Integer boardSeq;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Generated(GenerationTime.INSERT)
    @Column(name = "reg_dt", nullable = false, updatable = false, insertable = false
        , columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regDt;

    @Generated(GenerationTime.INSERT)
    @Column(name = "chg_dt", nullable = false, updatable = false, insertable = false
        , columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime chgDt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted;

    public Post(Integer seq, String title, String contents, Integer boardSeq, String userId, LocalDateTime regDt,
                LocalDateTime chgDt, boolean deleted)
    {
        this.seq = seq;
        this.title = title;
        this.contents = contents;
        this.boardSeq = boardSeq;
        this.userId = userId;
        this.regDt = regDt;
        this.chgDt = chgDt;
        this.deleted = deleted;
    }

    @Override
    public String toString()
    {
        return "Post{" +
            "seq=" + seq +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", boardSeq=" + boardSeq +
            ", userId='" + userId + '\'' +
            ", regDt=" + regDt +
            ", chgDt=" + chgDt +
            ", deleted=" + deleted +
            '}';
    }
}
