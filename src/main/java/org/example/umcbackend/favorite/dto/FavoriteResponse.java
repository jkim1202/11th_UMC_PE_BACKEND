package org.example.umcbackend.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.example.umcbackend.favorite.entity.Favorite;

public record FavoriteResponse(
        @Schema(description = "즐겨찾기 ID", example = "1")
        Long favoriteId,
        @Schema(description = "TMDB 영화 ID", example = "550")
        Long movieId,
        @Schema(description = "등록 일시", example = "2025-01-01T12:00:00")
        LocalDateTime createdAt
) {
    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getFavoriteId(), favorite.getMovieId(), favorite.getCreatedAt());
    }
}
