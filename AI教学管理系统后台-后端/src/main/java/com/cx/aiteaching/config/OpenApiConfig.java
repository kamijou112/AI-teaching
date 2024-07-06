package com.cx.aiteaching.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@AutoConfigureBefore(SpringDocConfiguration.class)
public class OpenApiConfig {

    private static final String TOKEN_HEADER = "Authorization";

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(info())
                .externalDocs(externalDocs())
                .components(components())
                .addSecurityItem(securityRequirement());
    }

    private Info info(){
        return new Info()
                .title("ai教学系统接口文档")
                .version("v0.0.1")
                .description("")
                .license(new License()
                        .name("Apache 2.0") // The Apache License, Version 2.0
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                .contact(new Contact()
                        .name("花散里")
                        .url("")
                        .email("2285441914@qq.com"))
                .termsOfService("");
    }

    private ExternalDocumentation externalDocs() {
        return new ExternalDocumentation()
                .description("ai教学系统接口文档")
                .url("http://localhost:9327/doc.html");
    }

    private Components components(){
        return new Components()
                .addSecuritySchemes("Bearer Authorization",
                        new SecurityScheme()
                                .name("Bearer 认证")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                )
                .addSecuritySchemes("Basic Authorization",
                        new SecurityScheme()
                                .name("Basic 认证")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                );

    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("Bearer Authorization");
    }

    private List<SecurityRequirement> security(Components components) {
        return components.getSecuritySchemes()
                .keySet()
                .stream()
                .map(k -> new SecurityRequirement().addList(k))
                .collect(Collectors.toList());
    }


    /**
     * 通用接口
     * @return
     */
    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                //.group("身份认证")
                .group("ai教学系统接口文档") // 分组名
                //.pathsToMatch("/api/**") // 按路径匹配
                .packagesToScan("com.cx.aiteaching.controller") //按指定的包路径匹配
                //.pathsToMatch("/api/user/**")
                // 为指定组设置请求头
                // .addOperationCustomizer(operationCustomizer())
                // 添加自定义配置，这里添加了一个用户认证的 header，否则 knife4j 里会没有 header
                /*.addOperationCustomizer((operation, handlerMethod) -> operation.security(
                        Collections.singletonList(new SecurityRequirement().addList(TOKEN_HEADER)))
                )*/
                .build();
    }

    /**
     * 一线人员
     * @return
     */
    /*@Bean
    public GroupedOpenApi chaserApi(){
        return GroupedOpenApi.builder()
                .group("一线人员")
                .pathsToMatch("/api/v1/chaser/**",
                        "/api/v1/experience/search/**",
                        "/api/v1/log/**",
                        "/api/v1/contact/**",
                        "/api/v1/admin/user/update")
                .pathsToExclude("/api/v1/experience/search/id")
                .build();
    }*/

    /**
     * 部门主管
     * @return
     */
    /*@Bean
    public GroupedOpenApi supervisorApi(){
        return GroupedOpenApi.builder()
                .group("部门主管")
                .pathsToMatch("/api/v1/supervisor/**",
                        "/api/v1/experience/**",
                        "/api/v1/schedule/**",
                        "/api/v1/contact/**",
                        "/api/v1/admin/user/update")
                .build();
    }*/

    /**
     * 系统管理员
     * @return
     */
    /*@Bean
    public GroupedOpenApi adminApi(){
        return GroupedOpenApi.builder()
                .group("系统管理员")
                .pathsToMatch("/api/v1/admin/**")
                // .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("京茶吉鹿接口—Admin")))
                .build();
    }*/
}

