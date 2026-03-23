package com.chatapp.dto;

import com.chatapp.enums.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRoomRequest(
        @NotBlank(message = "Room name cannot be blank")
        @Size(min = 2, max = 100, message = "Room name must be between 2 and 100 characters")
        String name,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        ChatRoomType type
) {
    public ChatRoomRequest {
        if (type == null) {
            type = ChatRoomType.PUBLIC;
        }
    }
}
