package ru.practicum.main_service.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.dto.CommentCreateDto;
import ru.practicum.main_service.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
@Validated
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addCommentByPrivate(@PathVariable Long userId,
                                          @RequestParam Long eventId,
                                          @Valid @RequestBody CommentCreateDto commentCreateDto) {
        return commentService.addCommentByPrivate(userId, eventId, commentCreateDto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateCommentByPrivate(@PathVariable Long userId,
                                             @PathVariable Long commentId,
                                             @Valid @RequestBody CommentCreateDto commentCreateDto) {
        return commentService.updateCommentByPrivate(userId, commentId, commentCreateDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByPrivate(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteCommentByPrivate(userId, commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> findCommentsByPrivate(
            @PathVariable Long userId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.findCommentsByPrivate(userId, eventId, PageRequest.of(from / size, size));
    }
}
