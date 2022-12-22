package com.colabear754.blog_example_kt.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.sql.Date
import java.sql.Timestamp

@Configuration
@EnableWebMvc
class SwaggerConfig {
    @Bean
    fun swaggerApi(): Docket = Docket(DocumentationType.SWAGGER_2)
        .consumes(getConsumeContentTypes())
        .produces(getProduceContentTypes())
        .apiInfo(swaggerInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.colabear754.blog_example_kt.controllers"))
        .paths(PathSelectors.any())
        .build()
        .useDefaultResponseMessages(false)
        .directModelSubstitute(Timestamp::class.java, Date::class.java)

    private fun swaggerInfo() = ApiInfoBuilder()
        .title("블로그 API")
        .description("Kotlin Spring Boot로 작성한 블로그 API")
        .version("1.0.0")
        .build()

    private fun getConsumeContentTypes(): Set<String> {
        val consumes = HashSet<String>()
        consumes.add("application/json;charset=UTF-8")
        return consumes
    }

    private fun getProduceContentTypes(): Set<String> {
        val produces = HashSet<String>()
        produces.add("application/json;charset=UTF-8")
        return produces
    }
}