package org.example.umcbackend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
public record LoginResponse(
        @Schema(description = "닉네임", example = "무비러버")
        String nickname,
        @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.payload.signature")
        String accessToken
) {
}
