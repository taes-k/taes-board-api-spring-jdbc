package com.taes.board.api.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_role", uniqueConstraints = {
    @UniqueConstraint(
        columnNames = {"user_id", "role"}
    )})
public class UserRole
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 10, nullable = false)
    private String role;

    @Builder
    public UserRole(Integer seq, User user, String role)
    {
        this.seq = seq;
        this.user = user;
        this.role = role;
    }

    @Override
    public String toString()
    {
        return "UserRole{" +
            "seq=" + seq +
            ", userid='" + user.getId() + '\'' +
            ", role='" + role + '\'' +
            '}';
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}