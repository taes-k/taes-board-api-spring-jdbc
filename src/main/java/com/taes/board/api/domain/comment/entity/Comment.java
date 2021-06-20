package com.taes.board.api.domain.comment.entity;

import com.taes.board.api.domain.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Log4j2
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comment")
@SQLDelete(sql = "UPDATE comment SET deleted=true WHERE seq=?")
@Where(clause = "deleted = false")
public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @Column(nullable = false, length = 200)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_seq", insertable=false, updatable=false)
    private Post post;

    @Column(name = "post_seq", nullable = false)
    private Integer postSeq;

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

    @Column(nullable = false)
    private boolean deleted;

    @Override
    public String toString()
    {
        return "Comment{" +
            "seq=" + seq +
            ", contents='" + contents + '\'' +
            ", postSeq=" + postSeq +
            ", userId='" + userId + '\'' +
            ", regDt=" + regDt +
            ", chgDt=" + chgDt +
            ", deleted=" + deleted +
            '}';
    }
}
