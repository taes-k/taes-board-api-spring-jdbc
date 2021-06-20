package com.taes.board.api.domain.user.service;

import com.taes.board.api.domain.user.dto.UserDto;
import com.taes.board.api.domain.user.entity.User;
import com.taes.board.api.domain.user.repository.UserRepository;
import com.taes.board.api.util.AuthUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(
        UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(String id)
    {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public User getUserSelf()
    {
        return getUser(AuthUtil.getUserId())
            .orElseThrow(() -> new IllegalStateException("유효한 사용자가 아닙니다."));
    }

    public boolean isExistUser(String id)
    {
        return userRepository.findOneByIdIncludeDeleted(id) != null;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public User setUser(User user)
    {
        return userRepository.save(user);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public User updateUser(User user, UserDto.UpdateReq userDto)
    {
        updateUserInfo(user, userDto);
        return user;
    }

    private void updateUserInfo(User user, UserDto.UpdateReq userDto)
    {
        if (StringUtils.isNotEmpty(userDto.getName()))
        {
            user.setName(userDto.getName());
        }
        if (StringUtils.isNotEmpty(userDto.getEmail()))
        {
            user.setEmail(userDto.getEmail());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public void deleteUser(String userId)
    {
        userRepository.deleteById(userId);
    }
}
