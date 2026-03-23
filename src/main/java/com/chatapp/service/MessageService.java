package com.chatapp.service;

import com.chatapp.dto.ChatMessageRequest;
import com.chatapp.dto.ChatMessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MessageService {

    ChatMessageResponse sendMessage(ChatMessageRequest request, UUID senderId);

    Page<ChatMessageResponse> getMessagesByChatRoom(UUID chatRoomId, Pageable pageable);

    void deleteMessage(UUID messageId, UUID requesterId);
}
