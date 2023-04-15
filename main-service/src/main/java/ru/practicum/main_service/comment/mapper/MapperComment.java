package ru.practicum.main_service.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.model.Comment;
import ru.practicum.main_service.user.mapper.MapperUser;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MapperComment {

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthor(MapperUser.toUserShortDto(comment.getAuthor()));
        commentDto.setEventId(comment.getEvent().getId());
        commentDto.setCreatedOn(comment.getCreatedOn());
        commentDto.setEditedOn(comment.getEditedOn());
        return commentDto;
    }

    public static List<CommentDto> toCommentDto(List<Comment> commentList) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtoList.add(toCommentDto(comment));
        }
        return commentDtoList;
    }
}
