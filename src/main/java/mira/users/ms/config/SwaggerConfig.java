package mira.users.ms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    Parameter authHeader = new ParameterBuilder()
            .parameterType("header")
            .name("Authorization")
            .modelRef(new ModelRef("string"))
            .build();

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("mira.users.ms.restcontrollers"))
                .paths(PathSelectors.regex("/api.*"))
                //.paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .globalOperationParameters(Collections.singletonList(authHeader));

    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Mira users",
                "User micro service",
                "v1",
                "",
                "polyashofff@yandex.ru",
                //heroku не билдит проект, если используется другой конструктор
                //new Contact("Алексей Поляшов","https://alexey-polyashov.github.io/brain-docs/", "polyashofff@yandex.ru"),
                "",
                "");
    }


}