package com.project.hirescopebackend.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HireScope API")
                        .description("AI 기반 이력서 자동 분석 서비스 - HireScope REST API 명세서\n\n" +
                                "## 인증 방식\n" +
                                "세션 기반 인증 사용. `/api/auth/login` 으로 로그인 후 발급된 **세션 쿠키(JSESSIONID)**를 이후 요청에 포함.\n\n" +
                                "## 역할\n" +
                                "- `APPLICANT`: 이력서 작성, 면접 질문 조회\n" +
                                "- `HR`: 직무 등록, 지원자 목록/점수 조회")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("HireScope Team")))
                .tags(List.of(
                        new Tag().name("Auth").description("회원가입 / 로그인 / 로그아웃"),
                        new Tag().name("Resume").description("이력서 CRUD (APPLICANT)"),
                        new Tag().name("Job").description("채용공고 관리 (HR)"),
                        new Tag().name("Application").description("지원 및 AI 분석 결과 조회")
                ));
    }
}
