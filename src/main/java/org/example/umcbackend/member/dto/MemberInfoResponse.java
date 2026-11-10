package org.example.umcbackend.member.dto;

import java.time.LocalDateTime;
import org.example.umcbackend.member.entity.Member;

public record MemberInfoResponse(
        String email,
        String nickname,
        LocalDateTime createdAt
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(member.getEmail(), member.getNickname(), member.getCreatedAt());
    }
}
