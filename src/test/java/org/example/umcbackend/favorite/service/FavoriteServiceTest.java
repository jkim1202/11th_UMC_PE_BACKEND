package org.example.umcbackend.favorite.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.example.umcbackend.favorite.dto.CreateFavoriteRequest;
import org.example.umcbackend.favorite.dto.FavoriteResponse;
import org.example.umcbackend.favorite.entity.Favorite;
import org.example.umcbackend.favorite.repository.FavoriteRepository;
import org.example.umcbackend.global.exception.ApiException;
import org.example.umcbackend.member.entity.Member;
import org.example.umcbackend.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long MOVIE_ID = 550L;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    @DisplayName("아직 즐겨찾기하지 않은 영화를 등록하면 저장하고 영화 ID를 반환한다")
    void createFavorite() {
        // given
        Member member = Member.builder().email("a@umc.com").password("pw").nickname("nick").build();
        given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.of(member));
        given(favoriteRepository.existsByMember_MemberIdAndMovieId(MEMBER_ID, MOVIE_ID)).willReturn(false);
        given(favoriteRepository.saveAndFlush(any(Favorite.class))).willAnswer(inv -> inv.getArgument(0));

        // when
        FavoriteResponse response = favoriteService.create(MEMBER_ID, new CreateFavoriteRequest(MOVIE_ID));

        // then
        assertThat(response.movieId()).isEqualTo(MOVIE_ID);
        verify(favoriteRepository).saveAndFlush(any(Favorite.class));
    }

    @Test
    @DisplayName("이미 즐겨찾기한 영화를 다시 등록하면 409 FAVORITE409_1 예외가 나고 저장하지 않는다")
    void createFavoriteWhenDuplicated() {
        // given
        Member member = Member.builder().email("a@umc.com").password("pw").nickname("nick").build();
        given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.of(member));
        given(favoriteRepository.existsByMember_MemberIdAndMovieId(MEMBER_ID, MOVIE_ID)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> favoriteService.create(MEMBER_ID, new CreateFavoriteRequest(MOVIE_ID)))
                .isInstanceOfSatisfying(ApiException.class, e -> {
                    assertThat(e.getStatus()).isEqualTo(HttpStatus.CONFLICT);
                    assertThat(e.getCode()).isEqualTo("FAVORITE409_1");
                });
        verify(favoriteRepository, never()).saveAndFlush(any(Favorite.class));
    }
}
