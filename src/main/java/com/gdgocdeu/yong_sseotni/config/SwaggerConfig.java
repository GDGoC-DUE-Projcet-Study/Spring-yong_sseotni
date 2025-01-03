package com.gdgocdeu.yong_sseotni.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host("3.36.22.27:8080") // 외부 IP 설정
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gdgocdeu.yong_sseotni"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("용썼니 API 명세서")
                .description("안드로이드 앱 <용썼니>의 API 명세서 입니다.")
                .version("1.0.0")
                .build();
    }
}
