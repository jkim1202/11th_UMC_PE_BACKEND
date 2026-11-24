package org.example.umcbackend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.umcbackend.member.entity.Member;

public record MemberResponse(
        @Schema(description = "회원 ID", example = "1")
        Long memberId,
        @Schema(description = "이메일", example = "user@example.com")
        String email,
        @Schema(description = "닉네임", example = "무비러버")
        String nickname
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getMemberId(), member.getEmail(), member.getNickname());
    }
}
