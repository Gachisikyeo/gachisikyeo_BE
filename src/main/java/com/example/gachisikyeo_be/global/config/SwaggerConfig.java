package com.example.gachisikyeo_be.global.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        //보안 방식
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        //API 기본 정보
        Info info = new Info()
                .title("Gachisikyeo API")
                .version("1.0")
                .description("같이시켜 API");

        return new OpenAPI()
                .info(info);
    }
}
