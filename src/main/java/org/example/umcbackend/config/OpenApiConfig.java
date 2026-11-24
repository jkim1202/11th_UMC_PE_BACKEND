package org.example.umcbackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
}
