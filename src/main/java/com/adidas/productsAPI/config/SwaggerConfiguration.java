package com.adidas.productsAPI.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Created by alzorign on 21/12/2017.
 */
@Configuration
@EnableSwagger2
@PropertySource("classpath:application.properties")
public class SwaggerConfiguration {

	@Value("${application.version}")
	private String apiVersion;

	@Value("${application.name}")
	private String apiName;

	@Value("${application.description}")
	private String apiDescription;

	private static final String GROUP_NAME = "productsAPI";
	private static final String BASE_PACKAGE = "com.adidas.productsAPI";
	private static final String CONTACT = ""; // @TODO

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				//uncimment lines below to add to the swagger-ui the possibility of adding the auth header directly from hte swagger-ui
				//.globalOperationParameters(
				//		Collections.singletonList(new ParameterBuilder().name("Authorization").description("Authorization header")
				//				.modelRef(new ModelRef("string")).parameterType("header").required(true).build()))
				.groupName(GROUP_NAME)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		System.out.println("****" + apiDescription);
		return new ApiInfoBuilder()
				.title(apiVersion)
				.description(apiDescription)
				.version(apiVersion)
				.contact(CONTACT)
				.build();
	}

}
