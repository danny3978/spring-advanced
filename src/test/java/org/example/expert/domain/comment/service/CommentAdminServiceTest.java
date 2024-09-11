package org.example.expert.domain.comment.service;


import org.example.expert.domain.comment.repository.CommentRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentAdminServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentAdminService commentAdminService;


    @Nested
    class DeleteComment{
        @Test
        void comment_삭제_테스트_정상(){
            long commentId = 1L;

            commentAdminService.deleteComment(commentId);

        }

    }


}
