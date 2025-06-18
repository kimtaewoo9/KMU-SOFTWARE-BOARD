package software.board.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.title("SOFTWARE-BOARD") // API 문서 제목
			.version("v1.0.0") // API 버전
			.description("SOFTWARE-BOARD API 명세서입니다."); // API에 대한 상세 설명

		// JWT 인증 방식 설정을 위한 SecurityScheme 정의
		String jwtSchemeName = "JWT_TOKEN";
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
		Components components = new Components()
			.addSecuritySchemes(jwtSchemeName, new SecurityScheme()
				.name(jwtSchemeName)
				.type(SecurityScheme.Type.HTTP) // HTTP 방식
				.scheme("bearer") // bearer 토큰 방식 사용
				.bearerFormat("JWT"));

		return new OpenAPI()
			.info(info)
			.addSecurityItem(securityRequirement)
			.components(components);
	}
}
