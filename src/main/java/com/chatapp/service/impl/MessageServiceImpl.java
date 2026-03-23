package com.chatapp.service.impl;

import com.chatapp.dto.ChatMessageRequest;
import com.chatapp.dto.ChatMessageResponse;
import com.chatapp.entity.ChatRoom;
import com.chatapp.entity.Message;
import com.chatapp.entity.User;
import com.chatapp.exception.ResourceNotFoundException;
import com.chatapp.exception.UnauthorizedException;
import com.chatapp.repository.ChatRoomRepository;
import com.chatapp.repository.MessageRepository;
import com.chatapp.repository.UserRepository;
import com.chatapp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatMessageResponse sendMessage(ChatMessageRequest request, UUID senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + senderId));

        ChatRoom chatRoom = chatRoomRepository.findByIdAndIsDeletedFalse(request.chatRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Chat room not found with id: " + request.chatRoomId()));

        Message message = Message.builder()
                .content(request.content())
                .type(request.type())
                .sender(sender)
                .chatRoom(chatRoom)
                .build();

        Message saved = messageRepository.save(message);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getMessagesByChatRoom(UUID chatRoomId, Pageable pageable) {
        if (!chatRoomRepository.existsById(chatRoomId)) {
            throw new ResourceNotFoundException("Chat room not found with id: " + chatRoomId);
        }
        return messageRepository
                .findAllByChatRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(chatRoomId, pageable)
                .map(this::toResponse);
    }

    @Override
    public void deleteMessage(UUID messageId, UUID requesterId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));

        if (!message.getSender().getId().equals(requesterId)) {
            throw new UnauthorizedException("Only the message sender can delete this message");
        }

        message.setDeleted(true);
        messageRepository.save(message);
    }

    private ChatMessageResponse toResponse(Message message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getContent(),
                message.getType(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getSender().getFullName(),
                message.getChatRoom().getId(),
                message.getChatRoom().getName(),
                message.getCreatedAt()
        );
    }
}
