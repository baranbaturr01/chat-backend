package com.chatapp.dto;

import com.chatapp.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChatMessageRequest(
        @NotBlank(message = "Content cannot be blank")
        @Size(max = 5000, message = "Message content cannot exceed 5000 characters")
        String content,

        @NotNull(message = "Chat room ID is required")
        UUID chatRoomId,

        MessageType type
) {
    public ChatMessageRequest {
        if (type == null) {
            type = MessageType.TEXT;
        }
    }
}
