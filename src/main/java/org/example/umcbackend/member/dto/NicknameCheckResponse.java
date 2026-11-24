package org.example.umcbackend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
public record NicknameCheckResponse(
        @Schema(description = "사용 가능 여부 (true면 사용 가능)", example = "true")
        boolean available
) {}
