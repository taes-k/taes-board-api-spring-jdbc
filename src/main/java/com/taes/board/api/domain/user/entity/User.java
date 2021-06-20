package com.taes.board.api.domain.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class User
{
    @Id
    @Column(length = 40)
    private String id;

    @Column(length = 400, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String email;

    @Generated(GenerationTime.INSERT)
    @Column(name = "reg_dt", nullable = false, updatable = false, insertable = false
        , columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regDt;

    @Column(nullable = false)
    private boolean deleted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRole> roles = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private UserRank userRank;

    public User(String id, String password, String name, String email, LocalDateTime regDt, boolean deleted,
                List<UserRole> roles)
    {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.regDt = regDt;
        this.deleted = deleted;
        this.roles = roles;
    }

    @Override
    public String toString()
    {
        return "User{" +
            "id='" + id + '\'' +
            ", password='" + password + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", regDt=" + regDt +
            ", deleted=" + deleted +
            ", roles=" + roles +
            '}';
    }

    public void setPasswordAsBcrypt(String password)
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = "{bcrypt}" + passwordEncoder.encode(password);
    }

    public void addRole(UserRole role)
    {
        if (this.roles == null)
            this.roles = new ArrayList<>();

        role.setUser(this);
        this.roles.add(role);
        log.debug("add role : {}", role);
    }

    public List<String> getRoleNames()
    {
        return roles.stream()
            .map(UserRole::getRole)
            .collect(Collectors.toList());
    }

    public Integer getRanking()
    {
        if (userRank == null)
            return null;

        return userRank.getRanking();
    }
}