package org.example.umcbackend.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.example.umcbackend.rating.entity.Rating;

public record RatingResponse(
        @Schema(description = "평점 ID", example = "1")
        Long ratingId,
        @Schema(description = "TMDB 영화 ID", example = "550")
        Long movieId,
        @Schema(description = "별점 (1~5)", example = "5")
        Integer score,
        @Schema(description = "후기", example = "인상 깊은 영화였어요.")
        String comment,
        @Schema(description = "등록 일시", example = "2025-01-01T12:00:00")
        LocalDateTime createdAt,
        @Schema(description = "수정 일시", example = "2025-01-01T12:00:00")
        LocalDateTime updatedAt
) {
    public static RatingResponse from(Rating rating) {
        return new RatingResponse(
                rating.getRatingId(),
                rating.getMovieId(),
                rating.getScore(),
                rating.getComment(),
                rating.getCreatedAt(),
                rating.getUpdatedAt()
        );
    }
}