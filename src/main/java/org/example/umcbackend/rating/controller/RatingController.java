package org.example.umcbackend.rating.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.umcbackend.global.interceptor.AuthInterceptor;
import org.example.umcbackend.global.response.ApiResponse;
import org.example.umcbackend.rating.dto.CreateRatingRequest;
import org.example.umcbackend.rating.dto.RatingDeleteResponse;
import org.example.umcbackend.rating.dto.RatingResponse;
import org.example.umcbackend.rating.dto.UpdateRatingRequest;
import org.example.umcbackend.rating.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "평점", description = "영화 평점 조회/등록/수정/삭제 API")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // 유저별 평점 조회는 공개 API라 토큰을 받지 않습니다.
    @Operation(summary = "회원별 평점 조회",
            description = "특정 회원이 남긴 평점 목록을 조회합니다. 토큰 없이 호출할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEMBER404_1 - 존재하지 않는 회원")
    })
    @GetMapping("/members/{memberId}/ratings")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getMemberRatings(@PathVariable Long memberId) {
        List<RatingResponse> ratings = ratingService.getRatingsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.ok("평점 조회에 성공했습니다.", ratings));
    }

    // memberId는 요청이 아니라 인터셉터가 토큰에서 꺼내 담아둔 값을 씁니다.
    @Operation(summary = "평점 등록",
            description = "영화에 평점(1~5)과 후기를 등록합니다. 한 영화에 한 번만 등록할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "COMMON4001 - 요청 값 검증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH401_2/3/4 - 토큰 없음, 잘못된 토큰 또는 만료된 토큰"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEMBER404_1 - 존재하지 않는 회원"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "RATING409_1 - 이미 평점을 등록한 영화")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/ratings")
    public ResponseEntity<ApiResponse<RatingResponse>> create(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @Valid @RequestBody CreateRatingRequest request
    ) {
        RatingResponse response = ratingService.create(memberId, request);
        return ResponseEntity.status(201).body(ApiResponse.created("평점 등록에 성공했습니다.", response));
    }

    @Operation(summary = "평점 수정",
            description = "내 평점의 별점과 후기를 수정합니다. comment를 보내지 않으면 기존 후기를 유지합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "COMMON4001 - 요청 값 검증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH401_2/3/4 - 토큰 없음, 잘못된 토큰 또는 만료된 토큰"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "RATING403_1 - 다른 회원의 평점"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "RATING404_1 - 존재하지 않는 평점")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<RatingResponse>> update(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request
    ) {
        RatingResponse response = ratingService.update(memberId, ratingId, request);
        return ResponseEntity.ok(ApiResponse.ok("평점 수정에 성공했습니다.", response));
    }

    // 공통 응답 형식을 지키기 위해 204 대신 200 + 삭제된 ratingId로 응답합니다.
    @Operation(summary = "평점 삭제",
            description = "내 평점을 삭제하고 삭제된 ratingId를 반환합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH401_2/3/4 - 토큰 없음, 잘못된 토큰 또는 만료된 토큰"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "RATING403_1 - 다른 회원의 평점"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "RATING404_1 - 존재하지 않는 평점")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<RatingDeleteResponse>> delete(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long ratingId
    ) {
        RatingDeleteResponse response = ratingService.delete(memberId, ratingId);
        return ResponseEntity.ok(ApiResponse.ok("평점 삭제에 성공했습니다.", response));
    }
}
