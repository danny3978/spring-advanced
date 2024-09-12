package org.example.expert.domain.comment.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.AuthUserArgumentResolver;
import org.example.expert.config.GlobalExceptionHandler;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentController controller;

    @MockBean
    private CommentService service;

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
    void 댓글_저장_테스트() throws Exception {
        given(resolver.supportsParameter(any())).willReturn(true);
        given(resolver.resolveArgument(any(),any(),any(),any())).willReturn(
                new AuthUser(1L, "email", UserRole.USER)
        );

        long todoId = 1L;
        CommentSaveRequest request = new CommentSaveRequest("contents");
        UserResponse userResponse = new UserResponse(1L,"email");
        CommentSaveResponse response = new CommentSaveResponse(1L,request.getContents(),userResponse);

        given(service.saveComment(any(),anyLong(),any(CommentSaveRequest.class))).willReturn(response);

        mockMvc.perform(post("/todos/{todoId}/comments",todoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());
    }


    @Test
    void 댓글_조회_테스트() throws Exception {
        given(resolver.supportsParameter(any())).willReturn(true);
        given(resolver.resolveArgument(any(),any(),any(),any())).willReturn(
                new AuthUser(1L, "email", UserRole.USER)
        );

        long todoId = 1L;
        UserResponse userResponse = new UserResponse(1L,"email");
        CommentResponse response = new CommentResponse(1L,"contents",userResponse);

        List<CommentResponse> responseList = new ArrayList<>();

        responseList.add(response);

        given(service.getComments(anyLong())).willReturn(responseList);

        mockMvc.perform(get("/todos/{todoId}/comments",todoId))
                .andExpect(status().isOk());
    }
}
