package org.example.expert.domain.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.AuthUserArgumentResolver;
import org.example.expert.config.GlobalExceptionHandler;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerController.class)
public class ManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ManagerController controller;

    @MockBean
    private ManagerService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthUserArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(resolver)
                .build();
    }


    @Test
    void 담장자_저장_테스트() throws Exception {
        given(resolver.supportsParameter(any())).willReturn(true);
        given(resolver.resolveArgument(any(),any(),any(),any())).willReturn(
                new AuthUser(1L, "email", UserRole.USER)
        );

        long todoId = 1L;
        ManagerSaveRequest request = new ManagerSaveRequest(1L);
        UserResponse userResponse = new UserResponse(1L,"email");
        ManagerSaveResponse response = new ManagerSaveResponse(1L,userResponse);

        given(service.saveManager(any(),anyLong(),any(ManagerSaveRequest.class))).willReturn(response);

        mockMvc.perform(post("/todos/{todoId}/managers",todoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))).andExpect(status().isOk());
    }

    @Test
    void 담장자_조회_테스트() throws Exception{
        given(resolver.supportsParameter(any())).willReturn(true);
        given(resolver.resolveArgument(any(),any(),any(),any())).willReturn(
                new AuthUser(1L, "email", UserRole.USER)
        );

        long todoId = 1L;
        UserResponse userResponse = new UserResponse(1L,"email");
        ManagerResponse response = new ManagerResponse(1L,userResponse);

        List<ManagerResponse> responses = new ArrayList<>();
        responses.add(response);

        given(service.getManagers(anyLong())).willReturn(responses);

        mockMvc.perform(get("/todos/{todoId}/managers",todoId)).andExpect(status().isOk());
    }


    @Test
    void 담장자_삭제_테스트() throws Exception {
        given(resolver.supportsParameter(any())).willReturn(true);
        given(resolver.resolveArgument(any(),any(),any(),any())).willReturn(
                new AuthUser(1L, "email", UserRole.USER)
        );

        long todoId = 1L;
        long managerId = 1L;

        willDoNothing().given(service).deleteManager(any(),anyLong(),anyLong());

        mockMvc.perform(delete("/todos/{todoId}/managers/{managerId}",todoId,managerId))
                .andExpect(status().isOk());
    }
}
