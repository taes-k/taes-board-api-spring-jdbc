package com.taes.board.api.domain.user.controller;

import com.taes.board.api.domain.user.dto.UserDto;
import com.taes.board.api.domain.user.entity.User;
import com.taes.board.api.domain.user.entity.UserRole;
import com.taes.board.api.domain.user.service.UserService;
import com.taes.board.api.util.ConstantsUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController
{
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper)
    {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public List<UserDto.Res> getAllUsers()
    {
        return userService.getAllUsers().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/self")
    public UserDto.Res getUserSelf()
    {
        User user = userService.getUserSelf();
        return convertToDto(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public UserDto.Res setUser(@Valid @RequestBody UserDto.CreateReq userDto)
    {
        if (userService.isExistUser(userDto.getId()))
            throw new IllegalArgumentException(ConstantsUtil.DUPLICATE_USER_EXCEPTION_MSG);

        User user = convertToEntity(userDto);
        user = userService.setUser(user);
        return convertToDto(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping()
    public UserDto.Res updateUser(@Valid@RequestBody UserDto.UpdateReq userDto)
    {
        User user = userService.getUser(userDto.getId())
            .orElseThrow(() -> new IllegalArgumentException(ConstantsUtil.NOT_FOUND_USER_EXCEPTION_MSG));

        user = userService.updateUser(user, userDto);
        return convertToDto(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping()
    public void deleteUser(@Valid @RequestBody UserDto.DeleteReq userDto)
    {
        userService.deleteUser(userDto.getId());
    }

    private User convertToEntity(UserDto.CreateReq userDto)
    {
        User user = modelMapper.typeMap(UserDto.CreateReq.class, User.class)
            .addMappings(mapper -> mapper.skip(User::setRoles))
            .map(userDto);

        userDto.getRoles().stream()
            .map(r -> UserRole.builder()
                .role(r)
                .build())
            .forEach(user::addRole);

        if (!StringUtils.isEmpty(userDto.getPassword()))
        {
            user.setPasswordAsBcrypt(userDto.getPassword());
        }

        log.debug("entity : {}", user);
        return user;
    }

    private UserDto.Res convertToDto(User user)
    {
        UserDto.Res userDto = modelMapper.typeMap(User.class, UserDto.Res.class)
            .addMappings(mapper -> mapper.map(User::getRoleNames, UserDto.Res::setRoles))
            .addMappings(mapper -> mapper.map(User::getRanking, UserDto.Res::setRank))
            .map(user);


        log.debug("dto : {}", userDto);
        return userDto;
    }

}
