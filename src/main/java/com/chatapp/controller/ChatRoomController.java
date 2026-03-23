package com.chatapp.controller;

import com.chatapp.dto.ApiResponse;
import com.chatapp.dto.ChatRoomRequest;
import com.chatapp.dto.ChatRoomResponse;
import com.chatapp.entity.User;
import com.chatapp.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat-rooms")
@RequiredArgsConstructor
@Tag(name = "Chat Rooms", description = "Chat room management endpoints")
public class ChatRoomController {

    private final ChatService chatService;

    @PostMapping
    @Operation(summary = "Create a new chat room")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createChatRoom(
            @Valid @RequestBody ChatRoomRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        ChatRoomResponse response = chatService.createChatRoom(request, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Chat room created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a chat room by ID")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> getChatRoom(
            @Parameter(description = "Chat room ID") @PathVariable UUID id
    ) {
        return ResponseEntity.ok(ApiResponse.success(chatService.getChatRoomById(id)));
    }

    @GetMapping
    @Operation(summary = "Get all chat rooms with pagination")
    public ResponseEntity<ApiResponse<Page<ChatRoomResponse>>> getAllChatRooms(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(chatService.getAllChatRooms(pageable)));
    }

    @GetMapping("/my-rooms")
    @Operation(summary = "Get chat rooms for the authenticated user")
    public ResponseEntity<ApiResponse<Page<ChatRoomResponse>>> getMyChatRooms(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(
                chatService.getChatRoomsByMember(currentUser.getId(), pageable)
        ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a chat room")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> updateChatRoom(
            @PathVariable UUID id,
            @Valid @RequestBody ChatRoomRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Chat room updated successfully",
                chatService.updateChatRoom(id, request, currentUser.getId())
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (soft delete) a chat room")
    public ResponseEntity<ApiResponse<Void>> deleteChatRoom(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        chatService.deleteChatRoom(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Chat room deleted successfully", null));
    }

    @PostMapping("/{id}/members/{userId}")
    @Operation(summary = "Add a member to a chat room")
    public ResponseEntity<ApiResponse<Void>> addMember(
            @PathVariable UUID id,
            @PathVariable UUID userId,
            @AuthenticationPrincipal User currentUser
    ) {
        chatService.addMember(id, userId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Member added successfully", null));
    }

    @DeleteMapping("/{id}/members/{userId}")
    @Operation(summary = "Remove a member from a chat room")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable UUID id,
            @PathVariable UUID userId,
            @AuthenticationPrincipal User currentUser
    ) {
        chatService.removeMember(id, userId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Member removed successfully", null));
    }
}
