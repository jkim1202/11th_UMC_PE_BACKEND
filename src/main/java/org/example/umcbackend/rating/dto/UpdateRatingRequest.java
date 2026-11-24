package org.example.umcbackend.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRatingRequest(
        @Schema(description = "수정할 별점 (1~5)", example = "4")
        @NotNull(message = "별점을 입력해주세요.")
        @Min(value = 1, message = "별점은 1점 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5점 이하여야 합니다.")
        Integer score,
        @Schema(description = "수정할 후기 (최대 500자). 보내지 않으면 기존 후기를 유지합니다.", example = "다시 봐도 좋아요.")
        @Size(max = 500, message = "후기는 500자 이하로 입력해주세요.")
        String comment
) {}
