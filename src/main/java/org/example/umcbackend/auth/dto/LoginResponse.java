package org.example.umcbackend.auth.dto;

public record LoginResponse(
        String nickname,
        String accessToken
) {
}
