package ru.practicum.main_service.comment.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main_service.comment.dto.CommentCreateDto;
import ru.practicum.main_service.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findCommentsByAdmin(Pageable pageable);

    void deleteCommentByAdmin(Long commentId);

    List<CommentDto> findCommentsByPrivate(Long userId, Long eventId, Pageable pageable);

    CommentDto addCommentByPrivate(Long userId, Long eventId, CommentCreateDto commentCreateDto);

    CommentDto updateCommentByPrivate(Long userId, Long commentId, CommentCreateDto commentCreateDto);

    void deleteCommentByPrivate(Long userId, Long commentId);

    List<CommentDto> findCommentsByPublic(Long eventId, Pageable pageable);

    CommentDto findCommentByPublic(Long commentId);
}
