package org.example.umcbackend.rating.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.umcbackend.global.interceptor.AuthInterceptor;
import org.example.umcbackend.global.response.ApiResponse;
import org.example.umcbackend.rating.dto.CreateRatingRequest;
import org.example.umcbackend.rating.dto.RatingResponse;
import org.example.umcbackend.rating.dto.UpdateRatingRequest;
import org.example.umcbackend.rating.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // 유저별 평점 조회는 공개 API라 토큰을 받지 않습니다.
    @GetMapping("/members/{memberId}/ratings")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getMemberRatings(@PathVariable Long memberId) {
        List<RatingResponse> ratings = ratingService.getRatingsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.ok("평점 조회에 성공했습니다.", ratings));
    }

    // memberId는 요청이 아니라 인터셉터가 토큰에서 꺼내 담아둔 값을 씁니다.
    @PostMapping("/ratings")
    public ResponseEntity<ApiResponse<RatingResponse>> create(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @Valid @RequestBody CreateRatingRequest request
    ) {
        RatingResponse response = ratingService.create(memberId, request);
        return ResponseEntity.status(201).body(ApiResponse.created("평점 등록에 성공했습니다.", response));
    }

    @PatchMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<RatingResponse>> update(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request
    ) {
        RatingResponse response = ratingService.update(memberId, ratingId, request);
        return ResponseEntity.ok(ApiResponse.ok("평점 수정에 성공했습니다.", response));
    }

    // 공통 응답 형식을 지키기 위해 204 대신 200 + result null로 응답합니다.
    @DeleteMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long ratingId
    ) {
        ratingService.delete(memberId, ratingId);
        return ResponseEntity.ok(ApiResponse.ok("평점 삭제에 성공했습니다.", null));
    }
}
