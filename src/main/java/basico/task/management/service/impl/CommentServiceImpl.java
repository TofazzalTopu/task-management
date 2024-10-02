package basico.task.management.service.impl;

import basico.task.management.exception.Messages;
import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.Comment;
import basico.task.management.repository.primary.CommentRepository;
import basico.task.management.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final Messages messages;

    @Override
    public Comment save(Comment comment) {
        comment.setCreatedDate(LocalDate.now());
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Long id, Comment comment) {
        Comment savedComment = findById(id);
        savedComment.setComment(comment.getComment());
        savedComment.setTaskId(comment.getTaskId());
        savedComment.setCreatedBy(comment.getCreatedBy());
        savedComment.setStatus(comment.getStatus());
        return commentRepository.save(savedComment);
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException(messages.get("comment.not.found")));
    }

    @Override
    public List<Comment> findAllByTaskId(Long taskId) {
        return commentRepository.findAllByTaskId(taskId);
    }

    @Override
    public List<Comment> findAllByCreatedById(Long createdById) {
        return commentRepository.findAllByCreatedById(createdById);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
