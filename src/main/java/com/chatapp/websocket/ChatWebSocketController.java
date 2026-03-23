package com.chatapp.websocket;

import com.chatapp.dto.ChatMessageRequest;
import com.chatapp.dto.ChatMessageResponse;
import com.chatapp.entity.User;
import com.chatapp.enums.MessageType;
import com.chatapp.exception.ResourceNotFoundException;
import com.chatapp.repository.ChatRoomRepository;
import com.chatapp.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;

    @Operation(summary = "Send a chat message via WebSocket")
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {
        User user = getAuthenticatedUser(principal);
        if (user == null) {
            log.warn("Unauthenticated attempt to send message");
            return;
        }

        chatRoomRepository.findByIdAndIsDeletedFalse(request.chatRoomId())
                .filter(room -> room.getMembers().stream().anyMatch(m -> m.getId().equals(user.getId())))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Chat room not found or user is not a member: " + request.chatRoomId()));

        ChatMessageResponse response = messageService.sendMessage(request, user.getId());
        messagingTemplate.convertAndSend(
                "/topic/chat/" + request.chatRoomId(),
                response
        );
        log.debug("Message sent by {} to room {}", user.getUsername(), request.chatRoomId());
    }

    @Operation(summary = "Join a chat room via WebSocket")
    @MessageMapping("/chat.join")
    public void joinRoom(@Payload ChatMessageRequest request, Principal principal) {
        User user = getAuthenticatedUser(principal);
        if (user == null) {
            log.warn("Unauthenticated attempt to join room");
            return;
        }

        ChatMessageResponse joinMessage = new ChatMessageResponse(
                UUID.randomUUID(),
                user.getUsername() + " joined the room",
                MessageType.JOIN,
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                request.chatRoomId(),
                null,
                java.time.LocalDateTime.now()
        );

        messagingTemplate.convertAndSend(
                "/topic/chat/" + request.chatRoomId(),
                joinMessage
        );
        log.debug("User {} joined room {}", user.getUsername(), request.chatRoomId());
    }

    private User getAuthenticatedUser(Principal principal) {
        if (principal instanceof Authentication authentication
                && authentication.getPrincipal() instanceof User user) {
            return user;
        }
        return null;
    }
}
