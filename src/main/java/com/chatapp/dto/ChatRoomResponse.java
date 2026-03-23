package com.chatapp.dto;

import com.chatapp.enums.ChatRoomType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ChatRoomResponse(
        UUID id,
        String name,
        String description,
        ChatRoomType type,
        UUID createdById,
        String createdByUsername,
        List<UserSummary> members,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record UserSummary(
            UUID id,
            String username,
            String fullName,
            String avatarUrl
    ) {}
}
