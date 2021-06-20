package com.taes.board.api.domain.user.repository;

import com.taes.board.api.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, String>
{
}
