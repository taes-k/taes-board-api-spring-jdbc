package com.taes.board.api.domain.user.service;

import com.taes.board.api.domain.user.dto.UserDto;
import com.taes.board.api.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService
{
    Optional<User> getUser(String id);

    List<User> getAllUsers();

    User getUserSelf();

    boolean isExistUser(String id);

    User setUser(User user);

    User updateUser(User user, UserDto.UpdateReq userDto);

    void deleteUser(String userId);
}
