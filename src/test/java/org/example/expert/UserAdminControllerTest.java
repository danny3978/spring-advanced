package org.example.expert;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.aop.AspectAdmin;
import org.example.expert.config.FilterConfig;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.user.controller.UserAdminController;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.service.UserAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({AspectAdmin.class, FilterConfig.class})
public class UserAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAdminService userAdminService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void 어드민_로그_테스트() throws Exception {
        // given
        Long userId = 1L;
        UserRoleChangeRequest request = new UserRoleChangeRequest();
        request.setNewRole("ROLE_ADMIN"); // 예시 데이터

        String jsonRequest = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/admin/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        // then
    }
}
