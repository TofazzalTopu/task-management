package basico.task.management.service;

import basico.task.management.model.primary.Comment;

import java.util.List;

public interface CommentService {
    Comment save(Comment comment);
    Comment update(Long id, Comment comment);
    Comment findById(Long id);
    List<Comment> findAllByTaskId(Long taskId);
    List<Comment> findAllByCreatedById(Long createdById);
    void deleteById(Long id);
}
