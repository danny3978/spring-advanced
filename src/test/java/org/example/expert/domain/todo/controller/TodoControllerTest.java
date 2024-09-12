package org.example.expert.domain.todo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.AuthUserArgumentResolver;
import org.example.expert.config.GlobalExceptionHandler;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoController controller;

    @MockBean
    private TodoService service;

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
    void 할일_저장_테스트() throws Exception {
        given(resolver.supportsParameter(any())).willReturn(true);
        given(resolver.resolveArgument(any(),any(),any(),any())).willReturn(
                new AuthUser(1L, "email", UserRole.USER)
        );

        TodoSaveRequest request = new TodoSaveRequest("tilte","contents");
        UserResponse userResponse = new UserResponse(1L,"email");
        TodoSaveResponse response = new TodoSaveResponse(1L,request.getTitle(),request.getContents(),"weather",userResponse);

        given(service.saveTodo(any(),any(TodoSaveRequest.class))).willReturn(response);

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        ).andExpect(status().isOk());
    }

    @Test
    void 할일_조회_테스트() throws Exception {
        given(resolver.supportsParameter(any())).willReturn(true);
        given(resolver.resolveArgument(any(),any(),any(),any())).willReturn(
                new AuthUser(1L, "email", UserRole.USER)
        );

        UserResponse userResponse = new UserResponse(1L,"email");
        TodoResponse response = new TodoResponse(1L,"title","contents","weather",userResponse,null,null);

        Pageable pageable = PageRequest.of(0, 1);
        Page<TodoResponse> page1 = new PageImpl<>(List.of(response), pageable, 1);

        given(service.getTodos(anyInt(),anyInt())).willReturn(page1);

        mockMvc.perform(get("/todos").param("page","1")
                .param("size","10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void 할일_단일_조회_테스트() throws Exception {
        given(resolver.supportsParameter(any())).willReturn(true);
        given(resolver.resolveArgument(any(),any(),any(),any())).willReturn(
                new AuthUser(1L, "email", UserRole.USER)
        );

        long todoId = 1L;

        UserResponse userResponse = new UserResponse(1L,"email");
        TodoResponse response = new TodoResponse(1L,"title","contents","weather",userResponse,null,null);


        given(service.getTodo(anyLong())).willReturn(response);

        mockMvc.perform(get("/todos/{todoId}",todoId)).andExpect(status().isOk());
    }


}
