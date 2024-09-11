package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;


    @Nested
    class Signup{
        @Test
        public void 회원가입_이메일_오류_테스트() {
            // given
            SignupRequest signupRequest = new SignupRequest("email", "password", "ROLE_USER");

            given(userRepository.existsByEmail(anyString())).willReturn(true);

            // when
            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
                authService.signup(signupRequest);
            });

            // then
            assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
        }

        @Test
        public void 회원가입_테스트_정상(){
            SignupRequest signupRequest = new SignupRequest("email", "password", "USER");

            given(userRepository.existsByEmail(anyString())).willReturn(false);

            String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

            UserRole userRole = UserRole.of(signupRequest.getUserRole());

            User newUser = new User(
                    signupRequest.getEmail(),
                    encodedPassword,
                    userRole
            );

            given(userRepository.save(any())).willReturn(newUser);

            SignupResponse response = authService.signup(signupRequest);

            assertNotNull(response);
        }


    }



    @Nested
    class Signin{
        @Test
        public void 로그인_이메일_오류_테스트(){
            SigninRequest signupRequest = new SigninRequest("email","password");

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                    authService.signin(signupRequest));

            assertEquals("가입되지 않은 유저입니다.", exception.getMessage());

        }

        @Test
        public void 로그인_비밀번호_오류_테스트(){
            SigninRequest signupRequest = new SigninRequest("email","password");
            User user = new User("email","password",UserRole.USER);

            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

            AuthException exception = assertThrows(AuthException.class, () ->
                    authService.signin(signupRequest));

        }

        @Test
        public void 로그인_테스트_정상(){
            SigninRequest signinRequest = new SigninRequest("email","password");
            User user = new User("email","password",UserRole.USER);

            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(anyString(),anyString())).willReturn(true);

            SigninResponse response = authService.signin(signinRequest);

            assertNotNull(response);

        }

    }

}
