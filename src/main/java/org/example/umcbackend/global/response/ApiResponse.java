package org.example.umcbackend.global.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 응답 형식")
public record ApiResponse<T>(
        @Schema(description = "성공 여부", example = "true")
        boolean isSuccess,
        @Schema(description = "응답 코드", example = "COMMON200")
        String code,
        @Schema(description = "응답 메시지", example = "요청에 성공했습니다.")
        String message,
        @Schema(description = "응답 데이터 (실패 시 null)")
        T result
) {
    public static <T> ApiResponse<T> created(String message, T result) {
        return new ApiResponse<>(true, "COMMON201", message, result);
    }

    public static <T> ApiResponse<T> ok(String message, T result) {
        return new ApiResponse<>(true, "COMMON200", message, result);
    }

    public static ApiResponse<Void> failure(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}
