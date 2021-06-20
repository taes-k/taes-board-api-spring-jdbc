package com.taes.board.api.domain.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_rank")
public class UserRank
{
    @Id
    @Column(name = "user_id")
    private String userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Integer score;

    @Column
    private Integer ranking;

    @Generated(GenerationTime.INSERT)
    @Column(name = "reg_dt", nullable = false, updatable = false, insertable = false
        , columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regDt;

    @Generated(GenerationTime.INSERT)
    @Column(name = "chg_dt", nullable = false, updatable = false, insertable = false
        , columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime chgDt;
}
