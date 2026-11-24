package org.example.umcbackend.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FavoriteStatusResponse(
        @Schema(description = "TMDB 영화 ID", example = "550")
        Long movieId,
        @Schema(description = "즐겨찾기 여부", example = "true")
        @JsonProperty("isFavorite") boolean isFavorite
) {}
