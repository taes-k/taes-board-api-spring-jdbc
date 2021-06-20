package com.taes.board.api.domain.post.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Log4j2
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ban_word")
@SQLDelete(sql = "UPDATE ban_word SET deleted=true WHERE seq=?")
@Where(clause = "deleted = false")
public class BanWord
{
    @Id
    private String word;

    @Column(name = "reg_dt", nullable = false, updatable = false, insertable = false
        , columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regDt;

    @Column(nullable = false)
    private boolean deleted;

    public BanWord(String word, LocalDateTime regDt, boolean deleted)
    {
        this.word = word;
        this.regDt = regDt;
        this.deleted = deleted;
    }
}
