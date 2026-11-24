package org.example.umcbackend.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
public record FavoriteDeleteResponse(
        @Schema(description = "즐겨찾기 해제된 TMDB 영화 ID", example = "550")
        Long movieId
) {}
