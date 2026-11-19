package org.example.umcbackend.favorite.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.umcbackend.favorite.dto.CreateFavoriteRequest;
import org.example.umcbackend.favorite.dto.FavoriteDeleteResponse;
import org.example.umcbackend.favorite.dto.FavoriteResponse;
import org.example.umcbackend.favorite.dto.FavoriteStatusResponse;
import org.example.umcbackend.favorite.entity.Favorite;
import org.example.umcbackend.favorite.repository.FavoriteRepository;
import org.example.umcbackend.global.exception.ApiException;
import org.example.umcbackend.member.entity.Member;
import org.example.umcbackend.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FavoriteResponse create(Long memberId, CreateFavoriteRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "MEMBER404_1", "해당 회원을 찾을 수 없습니다."));

        if (favoriteRepository.existsByMember_MemberIdAndMovieId(memberId, request.movieId())) {
            throw new ApiException(HttpStatus.CONFLICT, "FAVORITE409_1", "이미 이 영화를 즐겨찾기하셨습니다.");
        }

        Favorite favorite = Favorite.builder()
                .member(member)
                .movieId(request.movieId())
                .build();

        return FavoriteResponse.from(favoriteRepository.saveAndFlush(favorite));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getMyFavorites(Long memberId) {
        return favoriteRepository.findByMember_MemberIdOrderByFavoriteIdDesc(memberId).stream()
                .map(FavoriteResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public FavoriteStatusResponse getStatus(Long memberId, Long movieId) {
        return new FavoriteStatusResponse(movieId,
                favoriteRepository.existsByMember_MemberIdAndMovieId(memberId, movieId));
    }

    @Transactional
    public FavoriteDeleteResponse delete(Long memberId, Long movieId) {
        Favorite favorite = favoriteRepository.findByMember_MemberIdAndMovieId(memberId, movieId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "FAVORITE404_1",
                        "즐겨찾기한 영화를 찾을 수 없습니다."));

        favoriteRepository.delete(favorite);
        return new FavoriteDeleteResponse(movieId);
    }
}
