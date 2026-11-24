package org.example.umcbackend.movierecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.umcbackend.favorite.dto.FavoriteResponse;
import org.example.umcbackend.rating.dto.RatingResponse;

// 즐겨찾기를 함께 추가하지 않았다면 favorite은 null입니다.
public record MovieRecordResponse(
        @Schema(description = "저장된 평점")
        RatingResponse rating,
        @Schema(description = "저장된 즐겨찾기 (addToFavorites가 false면 null)", nullable = true)
        FavoriteResponse favorite
) {}
