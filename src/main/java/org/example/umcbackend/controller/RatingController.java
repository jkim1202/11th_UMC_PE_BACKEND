package org.example.umcbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.umcbackend.domain.dto.ApiResponse;
import org.example.umcbackend.domain.dto.CreateRatingRequest;
import org.example.umcbackend.domain.dto.RatingResponse;
import org.example.umcbackend.domain.dto.UpdateRatingRequest;
import org.example.umcbackend.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/{memberId}/ratings")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getMemberRatings(@PathVariable Long memberId) {
        List<RatingResponse> ratings = ratingService.getRatingsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.ok("평점 조회에 성공했습니다.", ratings));
    }

    @PostMapping("/{memberId}/ratings")
    public ResponseEntity<ApiResponse<Long>> create(
            @PathVariable Long memberId,
            @Valid @RequestBody CreateRatingRequest request
    ) {
        Long ratingId = ratingService.create(memberId, request);
        return ResponseEntity.status(201).body(ApiResponse.created("평점 등록에 성공했습니다.", ratingId));
    }

    @PatchMapping("/{memberId}/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable Long memberId,
            @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request
    ) {
        ratingService.update(ratingId, memberId, request);
        return ResponseEntity.ok(ApiResponse.ok("평점 수정에 성공했습니다.", null));
    }

    @DeleteMapping("/{memberId}/ratings/{ratingId}")
    public ResponseEntity<Void> delete(@PathVariable Long memberId, @PathVariable Long ratingId) {
        ratingService.delete(ratingId, memberId);
        return ResponseEntity.noContent().build();
    }
}