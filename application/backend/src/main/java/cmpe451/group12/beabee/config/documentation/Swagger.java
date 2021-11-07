package cmpe451.group12.beabee.config.documentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Config class for OpenAPI Specification.
 * url: http://localhost:8080/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class Swagger {
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cmpe451.group12"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }
    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "MENTORSHIP MANAGEMENT SYSTEM",
                null,
                "v1.0",
                null,
                new Contact("BeaBee Github Page","https://github.com/bounswe/2021SpringGroup12","vturgut68@gmail.com"),
                null,
                null,
                Collections.emptyList()
        );
    }
}