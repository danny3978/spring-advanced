package org.example.expert.domain.todo.service;


import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private WeatherClient weatherClient;
    @InjectMocks
    private TodoService todoService;


    @Nested
    class SaveTodo{
        @Test
        void todo_저장_테스트_정상(){
            TodoSaveRequest request = new TodoSaveRequest("title","contents");
            AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
            User user = User.fromAuthUser(authUser);

            Todo todo = new Todo(request.getTitle(),request.getContents(),"weather",user);

            given(todoRepository.save(any(Todo.class))).willReturn(todo);

            TodoSaveResponse response = todoService.saveTodo(authUser,request);

            assertNotNull(response);
        }
    }


    @Nested
    class GetTodo{
        @Test
        void todoPage_할일_조회_테스트_정상(){
            int page = 1;
            int size = 1;

            AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
            User user = User.fromAuthUser(authUser);
            Todo todo = new Todo("title","contents","weather",user);

            Pageable pageable = PageRequest.of(0, 1);
            Page<Todo> todos = new PageImpl<>(List.of(todo),pageable, 1);

            given(todoRepository.findAllByOrderByModifiedAtDesc(any(Pageable.class))).willReturn(todos);

            Page<TodoResponse> response = todoService.getTodos(page,size);

            assertNotNull(response);

        }

        @Test
        void todo_조회_오류_테스트(){
           long todoId = 1L;

            InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                    todoService.getTodo(todoId)
                    );

            assertEquals("Todo not found" , exception.getMessage());

        }

        @Test
        void todo_조회_테스트_정상(){
            long todoId = 1L;
            AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
            User user = User.fromAuthUser(authUser);
            Todo todo = new Todo("title","contents","weather",user);

            given(todoRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(todo));

            TodoResponse response = todoService.getTodo(todoId);

            assertNotNull(response);
            assertNotNull(response.getContents());
            assertNotNull(response.getTitle());
            assertNotNull(response.getWeather());
            assertNotNull(response.getUser());
            assertNotNull(response.getUser().getId());
            assertNotNull(response.getUser().getEmail());
        }

    }

}
