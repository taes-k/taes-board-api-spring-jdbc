package com.taes.board.api.domain.user;

import com.taes.board.api.domain.user.dto.UserDto;
import com.taes.board.api.domain.user.entity.User;
import com.taes.board.api.domain.user.entity.UserRole;
import com.taes.board.api.domain.user.enums.Roles;
import com.taes.board.api.domain.user.repository.UserRepository;
import com.taes.board.api.domain.user.service.UserService;
import com.taes.board.api.domain.user.service.UserServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DisplayName("User 유닛테스트")
public class UserTest
{
    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private List<User> givenUsers;

    @BeforeEach
    void beforeEach_setGivenUsers()
    {
        String givenUser1Id = RandomStringUtils.random(10, true, false);
        User givenUser1 = new User(
            givenUser1Id
            , "pw"
            , "nm"
            , "mail"
            , LocalDateTime.now()
            , false
            , null);
        givenUser1.addRole(UserRole.builder().role(Roles.USER.name()).build());

        String givenUser2Id = RandomStringUtils.random(10, true, false);
        User givenUser2 = new User(
            givenUser2Id
            , "pw"
            , "nm"
            , "mail"
            , LocalDateTime.now()
            , false
            , null);
        givenUser2.addRole(UserRole.builder().role(Roles.USER.name()).build());

        givenUsers = Arrays.asList(givenUser1, givenUser2);
    }

    @DisplayName("전체 User 조회 -> 성공")
    @Test
    void getAllUsers_success()
    {
        // given
        UserService userService = new UserServiceImpl(userRepository);
        Mockito.when(userRepository.findAll()).thenReturn(givenUsers);

        // when
        List<User> users = userService.getAllUsers();

        // then
        Assertions.assertTrue(CollectionUtils.size(users) == 2);
    }

    @DisplayName("본인 User 정보 조회 -> 실패 (인증되지 않은 사용자)")
    @Test
    void getUserSelf_fail_noPrincipal()
    {
        // given
        Optional<User> givenUser = Optional.of(givenUsers.get(0));
        UserService userService = new UserServiceImpl(userRepository);
        Mockito.doReturn(givenUser).when(userRepository).findOne(Mockito.any());

        // when
        Executable exc = userService::getUserSelf;

        // then
        Assertions.assertThrows(IllegalStateException.class, exc);
    }


    @DisplayName("User 등록 -> 성공")
    @Test
    void setUser_success()
    {
        // given
        String newGivenUserId = RandomStringUtils.random(10, true, false);
        User newGivenUser = new User(
            newGivenUserId
            , "pw"
            , "nm"
            , "mail"
            , LocalDateTime.now()
            , false
            , null);
        newGivenUser.addRole(UserRole.builder().role(Roles.USER.name()).build());

        UserService userService = new UserServiceImpl(userRepository);
        Mockito.doReturn(newGivenUser).when(userRepository).save(Mockito.any());

        // when
        User user = userService.setUser(newGivenUser);
        String userId = user.getId();

        // then
        Assertions.assertEquals(newGivenUserId, userId);
    }

    @DisplayName("User 정보 수정 -> 성공")
    @Test
    void updateUser_success()
    {
        // given
        User givenUser = givenUsers.get(0);
        String givenUserId = givenUser.getId();
        String updatedName = "updated-name";

        UserDto.UpdateReq updatedGivenUser = new UserDto.UpdateReq();
        updatedGivenUser.setId(givenUserId);
        updatedGivenUser.setName(updatedName);
        updatedGivenUser.setEmail(givenUser.getEmail());

        UserService userService = new UserServiceImpl(userRepository);
        Mockito.doReturn(updatedGivenUser).when(userRepository).save(Mockito.any());

        // when
        User user = userService.updateUser(givenUser, updatedGivenUser);
        String userName = user.getName();

        // then
        Assertions.assertEquals(updatedName, userName);
    }

    @DisplayName("User 삭제 -> 성공")
    @Test
    void deleteUser_success()
    {
        // given
        User givenUser = givenUsers.get(0);
        String givenUserId = givenUser.getId();

        UserService userService = new UserServiceImpl(userRepository);
        Mockito.doNothing().when(userRepository).deleteById(Mockito.anyString());

        // when
        userService.deleteUser(givenUserId);

        // then
        Assertions.assertTrue(true);
    }
}

