package org.example.umcbackend.favorite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.umcbackend.favorite.dto.CreateFavoriteRequest;
import org.example.umcbackend.favorite.dto.FavoriteDeleteResponse;
import org.example.umcbackend.favorite.dto.FavoriteResponse;
import org.example.umcbackend.favorite.dto.FavoriteStatusResponse;
import org.example.umcbackend.favorite.service.FavoriteService;
import org.example.umcbackend.global.interceptor.AuthInterceptor;
import org.example.umcbackend.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "즐겨찾기", description = "영화 즐겨찾기 API (모든 API에 토큰 필요)")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // memberId는 요청이 아니라 인터셉터가 토큰에서 꺼내 담아둔 값을 씁니다.
    @Operation(summary = "즐겨찾기 등록",
            description = "영화를 즐겨찾기에 등록합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "COMMON4001 - 요청 값 검증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH401_2/3/4 - 토큰 없음, 잘못된 토큰 또는 만료된 토큰"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEMBER404_1 - 존재하지 않는 회원"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "FAVORITE409_1 - 이미 즐겨찾기한 영화")
    })
    @PostMapping("/favorites")
    public ResponseEntity<ApiResponse<FavoriteResponse>> create(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @Valid @RequestBody CreateFavoriteRequest request
    ) {
        FavoriteResponse response = favoriteService.create(memberId, request);
        return ResponseEntity.status(201).body(ApiResponse.created("즐겨찾기를 등록했습니다.", response));
    }

    @Operation(summary = "내 즐겨찾기 목록 조회",
            description = "내 즐겨찾기 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH401_2/3/4 - 토큰 없음, 잘못된 토큰 또는 만료된 토큰")
    })
    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getMyFavorites(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId
    ) {
        return ResponseEntity.ok(ApiResponse.ok("내 즐겨찾기 목록을 조회했습니다.",
                favoriteService.getMyFavorites(memberId)));
    }

    @Operation(summary = "즐겨찾기 여부 조회",
            description = "특정 영화를 즐겨찾기했는지 확인합니다. 응답의 isFavorite으로 판단합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH401_2/3/4 - 토큰 없음, 잘못된 토큰 또는 만료된 토큰")
    })
    @GetMapping("/favorites/{movieId}")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> getStatus(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long movieId
    ) {
        return ResponseEntity.ok(ApiResponse.ok("즐겨찾기 여부를 조회했습니다.",
                favoriteService.getStatus(memberId, movieId)));
    }

    @Operation(summary = "즐겨찾기 해제",
            description = "영화를 즐겨찾기에서 해제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "해제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "AUTH401_2/3/4 - 토큰 없음, 잘못된 토큰 또는 만료된 토큰"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "FAVORITE404_1 - 즐겨찾기하지 않은 영화")
    })
    @DeleteMapping("/favorites/{movieId}")
    public ResponseEntity<ApiResponse<FavoriteDeleteResponse>> delete(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long movieId
    ) {
        return ResponseEntity.ok(ApiResponse.ok("즐겨찾기를 해제했습니다.",
                favoriteService.delete(memberId, movieId)));
    }
}
