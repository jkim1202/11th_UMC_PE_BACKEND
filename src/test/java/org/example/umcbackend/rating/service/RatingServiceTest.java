package org.example.umcbackend.rating.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.example.umcbackend.global.exception.ApiException;
import org.example.umcbackend.member.entity.Member;
import org.example.umcbackend.member.repository.MemberRepository;
import org.example.umcbackend.rating.dto.CreateRatingRequest;
import org.example.umcbackend.rating.dto.UpdateRatingRequest;
import org.example.umcbackend.rating.entity.Rating;
import org.example.umcbackend.rating.repository.RatingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RatingService ratingService;

    private Member memberWithId(Long memberId, String email, String nickname) {
        Member member = Member.builder().email(email).password("pw").nickname(nickname).build();
        ReflectionTestUtils.setField(member, "memberId", memberId);
        return member;
    }

    @Test
    @DisplayName("같은 영화에 평점을 중복 등록하면 409 RATING409_1 예외가 나고 저장하지 않는다")
    void createRatingWhenDuplicated() {
        // given
        Member member = memberWithId(1L, "a@umc.com", "nick");
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(ratingRepository.existsByMember_MemberIdAndMovieId(1L, 550L)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> ratingService.create(1L, new CreateRatingRequest(550L, 5, "좋아요")))
                .isInstanceOfSatisfying(ApiException.class, e -> {
                    assertThat(e.getStatus()).isEqualTo(HttpStatus.CONFLICT);
                    assertThat(e.getCode()).isEqualTo("RATING409_1");
                });
        verify(ratingRepository, never()).saveAndFlush(any(Rating.class));
    }

    @Test
    @DisplayName("남이 쓴 평점을 수정하려 하면 403 RATING403_1 예외가 나고 저장하지 않는다")
    void updateRatingOfOtherMember() {
        // given
        Member owner = memberWithId(2L, "owner@umc.com", "owner");
        Rating rating = Rating.builder().member(owner).movieId(550L).score(3).comment("보통").build();
        given(ratingRepository.findById(10L)).willReturn(Optional.of(rating));

        // when & then (요청자는 1번 회원, 평점 주인은 2번 회원)
        assertThatThrownBy(() -> ratingService.update(1L, 10L, new UpdateRatingRequest(1, "수정")))
                .isInstanceOfSatisfying(ApiException.class, e -> {
                    assertThat(e.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
                    assertThat(e.getCode()).isEqualTo("RATING403_1");
                });
        verify(ratingRepository, never()).saveAndFlush(any(Rating.class));
    }
}
