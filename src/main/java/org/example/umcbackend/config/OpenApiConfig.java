package org.example.umcbackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.example.umcbackend.global.swagger.ErrorExample;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    // 보안 요구(자물쇠)는 전역이 아니라 토큰이 필요한 컨트롤러/메서드에만 @SecurityRequirement로 붙입니다.
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UMCine API")
                        .description("영화 평점·즐겨찾기 서비스 UMCine의 백엔드 API 문서입니다. "
                                + "토큰이 필요한 API는 로그인 후 받은 accessToken을 Authorize 버튼에 입력해 호출하세요.")
                        .version("v1.0.0"))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("로그인 응답의 accessToken (Bearer 접두사 없이 토큰만 입력)")));
    }

    // "CODE - 설명" 형식 (축약형 AUTH401_2/3/4 포함). 여러 개는 " / "로 이어집니다.
    private static final Pattern ERROR_DESC = Pattern.compile(
            "([A-Z]+\\d+(?:_\\d+)?(?:/\\d+)*)\\s*-\\s*(.*?)(?=\\s+/\\s+[A-Z]+\\d+|$)");

    // 4xx 응답의 description에서 에러 코드를 뽑아 실패 형식 스키마와 코드별 예시로 content를 다시 만듭니다.
    @Bean
    public OperationCustomizer errorResponseCustomizer() {
        return (operation, handlerMethod) -> {
            if (operation.getResponses() == null) {
                return operation;
            }
            operation.getResponses().forEach((status, response) -> {
                if (status.startsWith("4")) {
                    applyErrorContent(response);
                }
            });
            return operation;
        };
    }

    private static void applyErrorContent(ApiResponse response) {
        if (response.getDescription() == null) {
            return;
        }
        Map<String, String> descriptions = new LinkedHashMap<>();
        Matcher matcher = ERROR_DESC.matcher(response.getDescription());
        while (matcher.find()) {
            String desc = matcher.group(2).trim();
            for (String code : expand(matcher.group(1))) {
                descriptions.put(code, desc);
            }
        }
        if (descriptions.isEmpty()) {
            return;
        }

        Map<String, Example> examples = new LinkedHashMap<>();
        descriptions.forEach((code, desc) -> {
            List<String> messages = ErrorExample.messagesOf(code);
            if (messages.isEmpty()) {
                messages = List.of(desc);
            }
            for (int i = 0; i < messages.size(); i++) {
                String name = messages.size() == 1 ? code : code + " (" + (i + 1) + ")";
                Map<String, Object> value = new LinkedHashMap<>();
                value.put("isSuccess", false);
                value.put("code", code);
                value.put("message", messages.get(i));
                value.put("result", null);
                examples.put(name, new Example().summary(name).value(value));
            }
        });

        Schema<?> schema = new ObjectSchema()
                .addProperty("isSuccess", new BooleanSchema().example(false))
                .addProperty("code", new StringSchema())
                .addProperty("message", new StringSchema())
                .addProperty("result", new Schema<>().description("실패 시 항상 null"));
        MediaType mediaType = new MediaType().schema(schema);
        mediaType.setExamples(examples);
        response.setContent(new Content().addMediaType("application/json", mediaType));
    }

    // AUTH401_2/3/4 -> AUTH401_2, AUTH401_3, AUTH401_4
    private static List<String> expand(String token) {
        List<String> codes = new ArrayList<>();
        String[] parts = token.split("/");
        codes.add(parts[0]);
        String prefix = parts[0].substring(0, parts[0].lastIndexOf('_') + 1);
        for (int i = 1; i < parts.length; i++) {
            codes.add(prefix + parts[i]);
        }
        return codes;
    }
}
