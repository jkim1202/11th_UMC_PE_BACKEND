package org.example.umcbackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.umcbackend.auth.dto.LoginRequest;
import org.example.umcbackend.auth.dto.LoginResponse;
import org.example.umcbackend.global.exception.ApiException;
import org.example.umcbackend.global.jwt.JwtUtil;
import org.example.umcbackend.member.entity.Member;
import org.example.umcbackend.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // 이메일이 없을 때와 비밀번호가 틀렸을 때 같은 응답을 줘서 가입 여부가 드러나지 않게 합니다.
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(this::invalidCredentials);

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw invalidCredentials();
        }

        String accessToken = jwtUtil.createAccessToken(member.getMemberId(), member.getEmail());
        return new LoginResponse(member.getNickname(), accessToken);
    }

    private ApiException invalidCredentials() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "AUTH401_1",
                "이메일 또는 비밀번호가 올바르지 않습니다.");
    }
}
