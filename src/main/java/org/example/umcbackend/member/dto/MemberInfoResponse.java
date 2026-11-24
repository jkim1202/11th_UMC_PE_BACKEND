package org.example.umcbackend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.example.umcbackend.member.entity.Member;

public record MemberInfoResponse(
        @Schema(description = "회원 ID", example = "1")
        Long memberId,
        @Schema(description = "이메일", example = "user@example.com")
        String email,
        @Schema(description = "닉네임", example = "무비러버")
        String nickname,
        @Schema(description = "가입 일시", example = "2025-01-01T12:00:00")
        LocalDateTime createdAt
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(member.getMemberId(), member.getEmail(), member.getNickname(), member.getCreatedAt());
    }
}
