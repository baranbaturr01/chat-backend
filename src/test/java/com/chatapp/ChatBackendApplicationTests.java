package com.chatapp;

import com.chatapp.dto.ApiResponse;
import com.chatapp.dto.ChatMessageRequest;
import com.chatapp.dto.ChatRoomRequest;
import com.chatapp.enums.ChatRoomType;
import com.chatapp.enums.MessageType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ChatBackendApplicationTests {

    @Test
    void apiResponseSuccessTest() {
        ApiResponse<String> response = ApiResponse.success("test data");
        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo("test data");
    }

    @Test
    void apiResponseErrorTest() {
        ApiResponse<Void> response = ApiResponse.error("error message");
        assertThat(response.success()).isFalse();
        assertThat(response.message()).isEqualTo("error message");
    }

    @Test
    void chatMessageRequestDefaultTypeTest() {
        ChatMessageRequest request = new ChatMessageRequest("Hello", UUID.randomUUID(), null);
        assertThat(request.type()).isEqualTo(MessageType.TEXT);
    }

    @Test
    void chatRoomRequestDefaultTypeTest() {
        ChatRoomRequest request = new ChatRoomRequest("Test Room", "desc", null);
        assertThat(request.type()).isEqualTo(ChatRoomType.PUBLIC);
    }
}

