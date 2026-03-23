package com.chatapp.dto;

import com.chatapp.enums.MessageType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageResponse(
        UUID id,
        String content,
        MessageType type,
        UUID senderId,
        String senderUsername,
        String senderFullName,
        UUID chatRoomId,
        String chatRoomName,
        LocalDateTime createdAt
) {}
