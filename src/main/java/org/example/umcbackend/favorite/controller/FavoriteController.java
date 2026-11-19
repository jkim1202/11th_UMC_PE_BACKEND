package org.example.umcbackend.favorite.controller;

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
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // memberId는 요청이 아니라 인터셉터가 토큰에서 꺼내 담아둔 값을 씁니다.
    @PostMapping("/favorites")
    public ResponseEntity<ApiResponse<FavoriteResponse>> create(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @Valid @RequestBody CreateFavoriteRequest request
    ) {
        FavoriteResponse response = favoriteService.create(memberId, request);
        return ResponseEntity.status(201).body(ApiResponse.created("즐겨찾기를 등록했습니다.", response));
    }

    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getMyFavorites(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId
    ) {
        return ResponseEntity.ok(ApiResponse.ok("내 즐겨찾기 목록을 조회했습니다.",
                favoriteService.getMyFavorites(memberId)));
    }

    @GetMapping("/favorites/{movieId}")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> getStatus(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long movieId
    ) {
        return ResponseEntity.ok(ApiResponse.ok("즐겨찾기 여부를 조회했습니다.",
                favoriteService.getStatus(memberId, movieId)));
    }

    @DeleteMapping("/favorites/{movieId}")
    public ResponseEntity<ApiResponse<FavoriteDeleteResponse>> delete(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long movieId
    ) {
        return ResponseEntity.ok(ApiResponse.ok("즐겨찾기를 해제했습니다.",
                favoriteService.delete(memberId, movieId)));
    }
}
