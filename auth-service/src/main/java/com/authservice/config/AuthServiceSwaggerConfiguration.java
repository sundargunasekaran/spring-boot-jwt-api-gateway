package com.authservice.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Sundar G
 * Date: 25/04/2021
 * Time: 10:22 PM
 */


@Configuration
@EnableSwagger2
public class AuthServiceSwaggerConfiguration extends WebMvcConfigurationSupport {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        	//	.ignoredParameterTypes(ProjectModel.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.authservice.controller"))
                .paths(regex("/.*"))
                .build()
                .apiInfo(metaData())
                .useDefaultResponseMessages(false)//
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()))
                //.securitySchemes(new ArrayList<>(Arrays.asList(new ApiKey("Bearer", "Authorization", "Header"))))//
                //.tags(new Tag("users", "Operations about users"))//
               // .tags(new Tag("ping", "Just a ping"))//
                .genericModelSubstitutes(Optional.class);
    }
    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Test REST API")
                .description("\"Test REST API\"")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                .contact(new Contact("LIMS", "https://www.LIMS.com", "LIMS@xxxxxx.com"))
                .build();
    }
    
    private Predicate<String> postPaths() {
		return or(regex("/.*"));
		//return or(regex("/nxgdata.*"), regex("/projects.*"));
	}
   
	@Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    
    private ApiKey apiKey() {
    	return new ApiKey("Bearer", "Authorization", "header");
    }
    
    
    //2.8.0
    private SecurityContext securityContext() {
    	return SecurityContext.builder().securityReferences(defaultAuth())
        .forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth() {
    	AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    	AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    	authorizationScopes[0] = authorizationScope;
    	return Arrays.asList(new SecurityReference("Bearer",authorizationScopes));
    }
    
    @Bean
    public SecurityConfiguration security() {
    return SecurityConfigurationBuilder.builder().scopeSeparator(",")
        .additionalQueryStringParams(null)
        .useBasicAuthenticationWithAccessCodeGrant(false).build();
    }
    //end
    
}
