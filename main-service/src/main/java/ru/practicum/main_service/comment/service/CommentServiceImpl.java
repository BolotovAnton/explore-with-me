package ru.practicum.main_service.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.dto.CommentCreateDto;
import ru.practicum.main_service.comment.mapper.MapperComment;
import ru.practicum.main_service.comment.model.Comment;
import ru.practicum.main_service.comment.repository.CommentRepository;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.service.EventService;
import ru.practicum.main_service.exception.WrongTermsException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final EventService eventService;
    private final CommentRepository commentRepository;

    @Override
    public List<CommentDto> findCommentsByAdmin(Pageable pageable) {
        List<CommentDto> commentDtoList = MapperComment.toCommentDto(commentRepository.findAll(pageable).toList());
        log.info("all comments have been found");
        return commentDtoList;
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        commentRepository.deleteById(commentId);
        log.info("comment with id = " + commentId + " has been deleted");
    }

    @Override
    @Transactional
    public CommentDto addCommentByPrivate(Long userId, Long eventId, CommentCreateDto commentCreateDto) {
        User user = userService.findUserById(userId);
        Event event = eventService.getEventById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new WrongTermsException("event is not published");
        }
        Comment comment = new Comment();
        comment.setText(commentCreateDto.getText());
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());
        log.info("comment has been added");
        return MapperComment.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateCommentByPrivate(Long userId, Long commentId, CommentCreateDto commentCreateDto) {
        userService.findUserById(userId);
        Comment comment = getCommentById(commentId);
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new WrongTermsException("user is not the owner of the comment");
        }
        comment.setText(commentCreateDto.getText());
        comment.setEditedOn(LocalDateTime.now());
        log.info("comment with id = " + commentId + " has been updated");
        return MapperComment.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteCommentByPrivate(Long userId, Long commentId) {
        userService.findUserById(userId);
        Comment comment = getCommentById(commentId);
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new WrongTermsException("user is not the owner of the comment");
        }
        commentRepository.deleteById(commentId);
        log.info("comment with id = " + commentId + " has been deleted");
    }

    @Override
    public List<CommentDto> findCommentsByPrivate(Long userId, Long eventId, Pageable pageable) {
        userService.findUserById(userId);
        List<Comment> comments;
        if (eventId != null) {
            eventService.getEventById(eventId);
            comments = commentRepository.findAllByAuthorIdAndEventId(userId, eventId);
        } else {
            comments = commentRepository.findAllByAuthorId(userId);
        }
        log.info("comments have been found");
        return MapperComment.toCommentDto(comments);
    }

    @Override
    public List<CommentDto> findCommentsByPublic(Long eventId, Pageable pageable) {
        eventService.getEventById(eventId);
        List<CommentDto> commentDtoList = MapperComment.toCommentDto(commentRepository.findAllByEventId(eventId, pageable));
        log.info("comments have been found");
        return commentDtoList;
    }

    @Override
    public CommentDto findCommentByPublic(Long commentId) {
        CommentDto commentDto = MapperComment.toCommentDto(getCommentById(commentId));
        log.info("comment with id = " + commentId + " has been found");
        return commentDto;
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("comment is not found"));
    }
}
