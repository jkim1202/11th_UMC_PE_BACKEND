package org.example.umcbackend.movierecord.service;

import lombok.RequiredArgsConstructor;
import org.example.umcbackend.favorite.dto.FavoriteResponse;
import org.example.umcbackend.favorite.entity.Favorite;
import org.example.umcbackend.favorite.repository.FavoriteRepository;
import org.example.umcbackend.global.exception.ApiException;
import org.example.umcbackend.member.entity.Member;
import org.example.umcbackend.member.repository.MemberRepository;
import org.example.umcbackend.movierecord.dto.CreateMovieRecordRequest;
import org.example.umcbackend.movierecord.dto.MovieRecordResponse;
import org.example.umcbackend.rating.dto.RatingResponse;
import org.example.umcbackend.rating.entity.Rating;
import org.example.umcbackend.rating.repository.RatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieRecordService {

    private final MemberRepository memberRepository;
    private final RatingRepository ratingRepository;
    private final FavoriteRepository favoriteRepository;

    // 평점과 즐겨찾기를 한 트랜잭션으로 묶어, 하나라도 실패하면 둘 다 저장하지 않습니다.
    @Transactional
    public MovieRecordResponse create(Long memberId, CreateMovieRecordRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "MEMBER404_1", "해당 회원을 찾을 수 없습니다."));

        boolean addToFavorites = Boolean.TRUE.equals(request.addToFavorites());

        if (ratingRepository.existsByMember_MemberIdAndMovieId(memberId, request.movieId())) {
            throw new ApiException(HttpStatus.CONFLICT, "RATING409_1", "이미 이 영화에 평점을 남기셨습니다.");
        }
        if (addToFavorites && favoriteRepository.existsByMember_MemberIdAndMovieId(memberId, request.movieId())) {
            throw new ApiException(HttpStatus.CONFLICT, "FAVORITE409_1", "이미 이 영화를 즐겨찾기하셨습니다.");
        }

        Rating rating = ratingRepository.saveAndFlush(Rating.builder()
                .member(member)
                .movieId(request.movieId())
                .score(request.score())
                .comment(request.comment())
                .build());

        FavoriteResponse favorite = null;
        if (addToFavorites) {
            favorite = FavoriteResponse.from(favoriteRepository.saveAndFlush(Favorite.builder()
                    .member(member)
                    .movieId(request.movieId())
                    .build()));
        }

        return new MovieRecordResponse(RatingResponse.from(rating), favorite);
    }
}
