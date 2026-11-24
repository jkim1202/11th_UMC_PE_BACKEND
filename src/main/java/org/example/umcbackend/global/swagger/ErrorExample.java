package org.example.umcbackend.global.swagger;

import java.util.List;
import java.util.Map;

// Swagger 실패 응답 예시용 에러 코드 -> 실제 메시지 매핑입니다. 메시지는 서비스/핸들러 코드의 값과 같아야 합니다.
public final class ErrorExample {

    private static final Map<String, List<String>> MESSAGES = Map.ofEntries(
            Map.entry("AUTH401_1", List.of("이메일 또는 비밀번호가 올바르지 않습니다.")),
            Map.entry("AUTH401_2", List.of("로그인이 필요한 요청입니다.")),
            Map.entry("AUTH401_3", List.of("유효하지 않은 토큰입니다.")),
            Map.entry("AUTH401_4", List.of("토큰이 만료되었습니다. 다시 로그인해주세요.")),
            Map.entry("MEMBER404_1", List.of("해당 회원을 찾을 수 없습니다.")),
            Map.entry("MEMBER409_1", List.of("이미 사용 중인 닉네임입니다.")),
            Map.entry("RATING403_1", List.of("본인이 작성한 평점만 수정하거나 삭제할 수 있습니다.")),
            Map.entry("RATING404_1", List.of("해당 평점을 찾을 수 없습니다.")),
            Map.entry("RATING409_1", List.of("이미 이 영화에 평점을 남기셨습니다.")),
            Map.entry("FAVORITE404_1", List.of("즐겨찾기한 영화를 찾을 수 없습니다.")),
            Map.entry("FAVORITE409_1", List.of("이미 이 영화를 즐겨찾기하셨습니다.")),
            // 필드별 검증 메시지는 동적이므로 대표 하나만 둡니다.
            Map.entry("COMMON4001", List.of("올바른 이메일 형식이 아닙니다.")),
            Map.entry("COMMON4092", List.of("이미 사용중인 이메일입니다.", "이미 사용중인 닉네임입니다."))
    );

    private ErrorExample() {
    }

    // 코드에 해당하는 예시 메시지 목록(없으면 빈 목록)
    public static List<String> messagesOf(String code) {
        return MESSAGES.getOrDefault(code, List.of());
    }
}
