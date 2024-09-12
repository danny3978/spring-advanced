package org.example.expert.domain.comment.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.comment.service.CommentAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentAdminController.class)
public class CommentAdminControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentAdminService commentAdminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 어드민_테스트() throws Exception {
        long commentId = 1L;

        willDoNothing().given(commentAdminService).deleteComment(anyLong());

        mockMvc.perform(delete("/admin/comments/{commentId}", commentId))
                .andExpect(status().isOk());
    }

}
