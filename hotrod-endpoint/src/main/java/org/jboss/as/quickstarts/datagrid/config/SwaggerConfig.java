package org.jboss.as.quickstarts.datagrid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public Docket customDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
//                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("org.jboss.as.quickstarts.datagrid.SpringBootRestApplication"))
                .paths(PathSelectors.regex("/v1/.*"))
                .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "RHDG HotRod Sample",
                "RHDG HotRod Sample",
                "v1",
                "Terms of service",
                "pinaki@pinakis.co.in",
                "License of API",
                "https://swagger.io/docs/");
        return apiInfo;
    }
}
