package org.example.expert.domain.user.service;


import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdminService userAdminService;

    @Nested
    class ChangeUserRole{

        @Test
        void 사용자권한_수정_사용자_오류_테스트(){
            UserRoleChangeRequest roleChangeRequest = new UserRoleChangeRequest("Admin");
            long userId = 1L;
            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                    userAdminService.changeUserRole(userId,roleChangeRequest)
                    );

            assertEquals("User not found",exception.getMessage());
        }


        @Test
        void 사용자권환_수정_테스트_정상(){
            AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
            User user = User.fromAuthUser(authUser);
            UserRoleChangeRequest roleChangeRequest = new UserRoleChangeRequest("Admin");

            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            userAdminService.changeUserRole(user.getId(),roleChangeRequest);
        }

    }



}
