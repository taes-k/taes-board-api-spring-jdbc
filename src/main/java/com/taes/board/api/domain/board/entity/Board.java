package com.taes.board.api.domain.board.entity;

import com.taes.board.api.domain.comment.entity.Comment;
import com.taes.board.api.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "board")
@SQLDelete(sql = "UPDATE board SET deleted=true WHERE seq=?")
@Where(clause = "deleted = false")
public class Board
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @Column(length = 100, nullable = false)
    private String title;

    @Column
    private String contents;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted;

    public Board(String title)
    {
        this.title = title;
    }

    @Builder
    public Board(Integer seq, String title, String contents, boolean deleted)
    {
        this.seq = seq;
        this.title = title;
        this.contents = contents;
        this.deleted = deleted;
    }

    @Override
    public String toString()
    {
        return "Board{" +
            "seq=" + seq +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", deleted=" + deleted +
            '}';
    }
}
