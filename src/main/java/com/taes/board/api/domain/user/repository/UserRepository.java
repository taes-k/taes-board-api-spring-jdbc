package com.taes.board.api.domain.user.repository;

import com.taes.board.api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String>
{
    @Query(value = "select 1 from user where id=:id limit 1", nativeQuery = true)
    Integer findOneByIdIncludeDeleted(@Param("id") String id);
}