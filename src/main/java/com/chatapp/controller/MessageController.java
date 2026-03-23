package com.chatapp.controller;

import com.chatapp.dto.ApiResponse;
import com.chatapp.dto.ChatMessageResponse;
import com.chatapp.entity.User;
import com.chatapp.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Message management endpoints")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{chatRoomId}")
    @Operation(summary = "Get messages for a chat room with pagination")
    public ResponseEntity<ApiResponse<Page<ChatMessageResponse>>> getMessages(
            @Parameter(description = "Chat room ID") @PathVariable UUID chatRoomId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "50") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(
                messageService.getMessagesByChatRoom(chatRoomId, pageable)
        ));
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "Delete (soft delete) a message")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(
            @PathVariable UUID messageId,
            @AuthenticationPrincipal User currentUser
    ) {
        messageService.deleteMessage(messageId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Message deleted successfully", null));
    }
}
