package com.chatapp.repository;

import com.chatapp.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    Page<ChatRoom> findAllByIsDeletedFalse(Pageable pageable);

    Optional<ChatRoom> findByIdAndIsDeletedFalse(UUID id);

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.members m WHERE m.id = :userId AND cr.isDeleted = false")
    Page<ChatRoom> findAllByMemberIdAndIsDeletedFalse(@Param("userId") UUID userId, Pageable pageable);
}
