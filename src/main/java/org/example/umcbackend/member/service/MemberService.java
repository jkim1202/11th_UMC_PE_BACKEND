package org.example.umcbackend.member.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.umcbackend.global.exception.ApiException;
import org.example.umcbackend.member.dto.EmailCheckResponse;
import org.example.umcbackend.member.dto.MemberInfoResponse;
import org.example.umcbackend.member.dto.MemberResponse;
import org.example.umcbackend.member.dto.MemberUpdateRequest;
import org.example.umcbackend.member.dto.NicknameCheckResponse;
import org.example.umcbackend.member.dto.SignUpRequest;
import org.example.umcbackend.member.entity.Member;
import org.example.umcbackend.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public NicknameCheckResponse checkNickname(String nickname) {
        boolean exists = memberRepository.existsByNickname(nickname);
        return new NicknameCheckResponse(!exists);
    }

    @Transactional(readOnly = true)
    public EmailCheckResponse checkEmail(String email) {
        boolean exists = memberRepository.existsByEmail(email);
        return new EmailCheckResponse(!exists);
    }

    @Transactional
    public MemberResponse signUp(SignUpRequest request) {
        if(memberRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if(memberRepository.existsByNickname(request.nickname())){
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .build();

        Member savedmember = memberRepository.save(member);

        return MemberResponse.from(savedmember);
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMyInfo(Long memberId) {
        return MemberInfoResponse.from(findMember(memberId));
    }

    @Transactional
    public MemberInfoResponse updateMyInfo(Long memberId, MemberUpdateRequest request) {
        Member member = findMember(memberId);

        // DB UNIQUE 제약 위반 대신 우리가 정한 응답을 주기 위해 먼저 확인합니다.
        if (memberRepository.existsByNicknameAndMemberIdNot(request.nickname(), memberId)) {
            throw new ApiException(HttpStatus.CONFLICT, "MEMBER409_1", "이미 사용 중인 닉네임입니다.");
        }

        member.updateNickname(request.nickname());
        return MemberInfoResponse.from(member);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "MEMBER404_1",
                        "해당 회원을 찾을 수 없습니다."));
    }
}
