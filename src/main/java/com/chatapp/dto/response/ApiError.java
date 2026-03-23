package com.chatapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response wrapper")
public class ApiError {

    @Schema(description = "HTTP status code", example = "400")
    private Integer status;

    @Schema(description = "Human-readable error message", example = "Validation failed")
    private String message;

    @Schema(description = "List of detailed error messages (e.g. field validation errors)")
    private List<String> errors;

    @Builder.Default
    @Schema(description = "Timestamp of the error")
    private LocalDateTime timestamp = LocalDateTime.now();
}
