package ru.practicum.shareit.item.dto;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;

import static ru.practicum.shareit.utils.Exceptions.getNoSuchElementException;

@Service
public class CommentMapper {

    private final UserService userService;
    private final ItemService itemService;

    public CommentMapper(@Lazy UserService userService, @Lazy ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    public static CommentGetDto toGetDto(Comment comment) {
        return new CommentGetDto(
                comment.getId(),
                comment.getAuthor().getName(),
                comment.getText(),
                comment.getCreated()
        );
    }

    public Comment toComment(CommentInsertDto dto) {
        return new Comment(
                null,
                dto.getText(),
                itemService.findById(dto.getItemId())
                        .orElseThrow(() -> getNoSuchElementException("item", dto.getItemId())),
                userService.findById(dto.getAuthorId())
                        .orElseThrow(() -> getNoSuchElementException("user", dto.getAuthorId())),
                Instant.now()
        );
    }
}
