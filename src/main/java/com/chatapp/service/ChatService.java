package com.chatapp.service;

import com.chatapp.dto.ChatRoomRequest;
import com.chatapp.dto.ChatRoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ChatService {

    ChatRoomResponse createChatRoom(ChatRoomRequest request, UUID creatorId);

    ChatRoomResponse getChatRoomById(UUID id);

    Page<ChatRoomResponse> getAllChatRooms(Pageable pageable);

    Page<ChatRoomResponse> getChatRoomsByMember(UUID userId, Pageable pageable);

    ChatRoomResponse updateChatRoom(UUID id, ChatRoomRequest request, UUID requesterId);

    void deleteChatRoom(UUID id, UUID requesterId);

    void addMember(UUID chatRoomId, UUID userId, UUID requesterId);

    void removeMember(UUID chatRoomId, UUID userId, UUID requesterId);
}
