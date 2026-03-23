package com.chatapp.service.impl;

import com.chatapp.dto.ChatRoomRequest;
import com.chatapp.dto.ChatRoomResponse;
import com.chatapp.entity.ChatRoom;
import com.chatapp.entity.User;
import com.chatapp.exception.ResourceNotFoundException;
import com.chatapp.exception.UnauthorizedException;
import com.chatapp.repository.ChatRoomRepository;
import com.chatapp.repository.UserRepository;
import com.chatapp.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public ChatRoomResponse createChatRoom(ChatRoomRequest request, UUID creatorId) {
        User creator = findUserById(creatorId);

        ChatRoom chatRoom = ChatRoom.builder()
                .name(request.name())
                .description(request.description())
                .type(request.type())
                .createdBy(creator)
                .build();
        chatRoom.getMembers().add(creator);

        ChatRoom saved = chatRoomRepository.save(chatRoom);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ChatRoomResponse getChatRoomById(UUID id) {
        ChatRoom chatRoom = chatRoomRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat room not found with id: " + id));
        return toResponse(chatRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatRoomResponse> getAllChatRooms(Pageable pageable) {
        return chatRoomRepository.findAllByIsDeletedFalse(pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatRoomResponse> getChatRoomsByMember(UUID userId, Pageable pageable) {
        return chatRoomRepository.findAllByMemberIdAndIsDeletedFalse(userId, pageable)
                .map(this::toResponse);
    }

    @Override
    public ChatRoomResponse updateChatRoom(UUID id, ChatRoomRequest request, UUID requesterId) {
        ChatRoom chatRoom = chatRoomRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat room not found with id: " + id));

        if (chatRoom.getCreatedBy() == null || !chatRoom.getCreatedBy().getId().equals(requesterId)) {
            throw new UnauthorizedException("Only the chat room creator can update this room");
        }

        chatRoom.setName(request.name());
        chatRoom.setDescription(request.description());
        chatRoom.setType(request.type());

        return toResponse(chatRoomRepository.save(chatRoom));
    }

    @Override
    public void deleteChatRoom(UUID id, UUID requesterId) {
        ChatRoom chatRoom = chatRoomRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat room not found with id: " + id));

        if (chatRoom.getCreatedBy() == null || !chatRoom.getCreatedBy().getId().equals(requesterId)) {
            throw new UnauthorizedException("Only the chat room creator can delete this room");
        }

        chatRoom.setDeleted(true);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void addMember(UUID chatRoomId, UUID userId, UUID requesterId) {
        ChatRoom chatRoom = chatRoomRepository.findByIdAndIsDeletedFalse(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat room not found with id: " + chatRoomId));

        boolean requesterIsMember = chatRoom.getMembers().stream()
                .anyMatch(m -> m.getId().equals(requesterId));
        if (!requesterIsMember) {
            throw new UnauthorizedException("Only chat room members can add new members");
        }

        User user = findUserById(userId);

        if (!chatRoom.getMembers().contains(user)) {
            chatRoom.getMembers().add(user);
            chatRoomRepository.save(chatRoom);
        }
    }

    @Override
    public void removeMember(UUID chatRoomId, UUID userId, UUID requesterId) {
        ChatRoom chatRoom = chatRoomRepository.findByIdAndIsDeletedFalse(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat room not found with id: " + chatRoomId));

        boolean isCreator = chatRoom.getCreatedBy() != null && chatRoom.getCreatedBy().getId().equals(requesterId);
        boolean isSelf = requesterId.equals(userId);
        if (!isCreator && !isSelf) {
            throw new UnauthorizedException("Only the chat room creator or the member themselves can remove a member");
        }

        User user = findUserById(userId);

        chatRoom.getMembers().remove(user);
        chatRoomRepository.save(chatRoom);
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private ChatRoomResponse toResponse(ChatRoom chatRoom) {
        List<ChatRoomResponse.UserSummary> memberSummaries = chatRoom.getMembers().stream()
                .map(m -> new ChatRoomResponse.UserSummary(
                        m.getId(),
                        m.getUsername(),
                        m.getFullName(),
                        m.getAvatarUrl()
                ))
                .toList();

        return new ChatRoomResponse(
                chatRoom.getId(),
                chatRoom.getName(),
                chatRoom.getDescription(),
                chatRoom.getType(),
                chatRoom.getCreatedBy() != null ? chatRoom.getCreatedBy().getId() : null,
                chatRoom.getCreatedBy() != null ? chatRoom.getCreatedBy().getUsername() : null,
                memberSummaries,
                chatRoom.getCreatedAt(),
                chatRoom.getUpdatedAt()
        );
    }
}
