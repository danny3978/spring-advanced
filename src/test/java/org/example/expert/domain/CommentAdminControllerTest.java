package org.example.expert.domain;

import org.example.expert.aop.AspectAdmin;
import org.example.expert.config.FilterConfig;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.comment.service.CommentAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
@Import({AspectAdmin.class, FilterConfig.class})
public class CommentAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentAdminService commentAdminService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void 어드민_로그_테스트() throws Exception {
        // given
        long commentId = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(delete("/admin/comments/{commentId}", commentId));

        // then
        // 로그가 정상적으로 출력되었는지 확인하기 위해 콘솔을 확인하세요.
    }
}
