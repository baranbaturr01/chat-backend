package com.chatapp.dto.response;

import com.chatapp.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User information response")
public class UserResponse {

    @Schema(description = "User unique identifier")
    private UUID id;

    @Schema(description = "Username", example = "johndoe")
    private String username;

    @Schema(description = "User email address", example = "john@example.com")
    private String email;

    @Schema(description = "User role", example = "USER")
    private Role role;

    @Schema(description = "Account creation timestamp")
    private LocalDateTime createdAt;
}
