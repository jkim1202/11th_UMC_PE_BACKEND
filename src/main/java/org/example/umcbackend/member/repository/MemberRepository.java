package org.example.umcbackend.member.repository;

import java.util.Optional;
import org.example.umcbackend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    // 나를 제외한 다른 회원이 이 닉네임을 쓰고 있는지 확인합니다.
    boolean existsByNicknameAndMemberIdNot(String nickname, Long memberId);
}
