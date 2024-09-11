package org.example.expert.domain.user.service;


import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Nested
    class GetUser{

        @Test
        void 사용자_조회_사용자_오류_테스트(){
            long userId = 1L;

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                    userService.getUser(userId)
                    );

            assertEquals("User not found",exception.getMessage());
        }


        @Test
        void 사용자_조회_테스트_정상(){
            long userId = 1L;
            User user = new User("email","password", UserRole.USER);

            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            UserResponse response = userService.getUser(userId);

            assertNotNull(response);
        }
    }

    @Nested
    class ChangePassword{
        @Test
        void 비밀번호_변경_비밀번호_길이_체크_오류_테스트(){
            long userId = 1L;
            UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword","1");

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                    userService.changePassword(userId,request)
            );

            assertEquals("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.", exception.getMessage());
        }

        @Test
        void 비밀번호_변경_비밀번호_숫자_체크_오류_테스트(){
            long userId = 1L;
            UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword","QWeasdzxc");

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                    userService.changePassword(userId,request)
            );

            assertEquals("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.", exception.getMessage());
        }

        @Test
        void 비밀번호_변경_비밀번호_대문자_체크_오류_테스트(){
            long userId = 1L;
            UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword","qweasd123");

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                    userService.changePassword(userId,request)
            );

            assertEquals("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.", exception.getMessage());
        }

        @Test
        void 사용자_오류_테스트(){
            long userId = 1L;

            UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword","QWEasd123");

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                    userService.changePassword(userId,request)
            );

            assertEquals("User not found",exception.getMessage());
        }


        @Test
        void 기존_비밀번호_새로운_비밀번호_체크_오류_테스트(){
            long userId = 1L;
            String rawPassword = "Qweasd123";
            String encodedPassword = passwordEncoder.encode(rawPassword);
            User user = new User("email", encodedPassword, UserRole.USER);

            UserChangePasswordRequest request = new UserChangePasswordRequest(rawPassword, rawPassword);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                    userService.changePassword(userId, request)
            );

            assertEquals("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.", exception.getMessage());
        }


        @Test
        void 기존_비밀번호_매치_오류_테스트(){
            long userId = 1L;
            String rawPassword = "Qweasd123";
            String encodedPassword = passwordEncoder.encode(rawPassword);
            User user = new User("email", encodedPassword, UserRole.USER);

            UserChangePasswordRequest request = new UserChangePasswordRequest("QWEasd123", "QWEasd123");

            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                    userService.changePassword(userId, request)
            );

            assertEquals("잘못된 비밀번호입니다.", exception.getMessage());
        }

        @Test
        void 비밀번호_변경_테스트_정상(){
            long userId = 1L;
            String rawPassword = "Qweasd123";
            String encodedPassword = passwordEncoder.encode(rawPassword);
            User user = new User("email", encodedPassword, UserRole.USER);
            UserChangePasswordRequest request = new UserChangePasswordRequest(rawPassword, "QWEasd123");

            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            userService.changePassword(userId,request);
        }

    }
}
