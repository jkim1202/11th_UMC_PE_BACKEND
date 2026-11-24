package org.example.umcbackend.movierecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.umcbackend.global.interceptor.AuthInterceptor;
import org.example.umcbackend.global.response.ApiResponse;
import org.example.umcbackend.movierecord.dto.CreateMovieRecordRequest;
import org.example.umcbackend.movierecord.dto.MovieRecordResponse;
import org.example.umcbackend.movierecord.service.MovieRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "영화 기록", description = "평점과 즐겨찾기를 한 번에 저장하는 API (토큰 필요)")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class MovieRecordController {

    private final MovieRecordService movieRecordService;

    @Operation(summary = "영화 기록 저장",
            description = "평점(및 후기)과 즐겨찾기를 한 번에 저장합니다. 하나라도 실패하면 평점·즐겨찾기 모두 저장되지 않습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "저장 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "COMMON4001 - 요청 값 검증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH401_2/3/4 - 토큰 없음, 잘못된 토큰 또는 만료된 토큰"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEMBER404_1 - 존재하지 않는 회원"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "RATING409_1 - 이미 평점을 등록한 영화 / FAVORITE409_1 - 이미 즐겨찾기한 영화")
    })
    @PostMapping("/movie-records")
    public ResponseEntity<ApiResponse<MovieRecordResponse>> create(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @Valid @RequestBody CreateMovieRecordRequest request
    ) {
        MovieRecordResponse response = movieRecordService.create(memberId, request);
        return ResponseEntity.status(201).body(ApiResponse.created("영화 기록을 저장했습니다.", response));
    }
}
