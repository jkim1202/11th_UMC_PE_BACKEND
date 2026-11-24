package org.example.umcbackend.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
public record RatingDeleteResponse(
        @Schema(description = "삭제된 평점 ID", example = "1")
        Long ratingId
) {}
