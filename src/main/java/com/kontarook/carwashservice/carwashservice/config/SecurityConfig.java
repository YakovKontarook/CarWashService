package com.kontarook.carwashservice.carwashservice.config;

import com.kontarook.carwashservice.carwashservice.security.jwt.JwtConfigurer;
import com.kontarook.carwashservice.carwashservice.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String LOGIN_ENDPOINT = "/carwash/api/auth/**";
    private static final String APPOINTMENT_ENDPOINT = "/carwash/api/appointments/**";
    private static final String ASSISTANCE_ENDPOINT = "/carwash/api/assistance/**";
    private static final String ASSISTANCE_ADMIN_ENDPOINT = "/carwash/api/assistance/admin/**";
    private static final String APPOINTMENTS_USER_ENDPOINT = "/carwash/api/appointments/user/**";
    private static final String APPOINTMENTS_ADMIN_ENDPOINT = "/carwash/api/appointments/admin/**";


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.POST, APPOINTMENT_ENDPOINT).hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, APPOINTMENTS_ADMIN_ENDPOINT).hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, ASSISTANCE_ENDPOINT).hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, ASSISTANCE_ENDPOINT, APPOINTMENTS_USER_ENDPOINT).hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, APPOINTMENT_ENDPOINT, ASSISTANCE_ENDPOINT).hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, ASSISTANCE_ADMIN_ENDPOINT).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",

            "/v3/api-docs/**",
            "/swagger-ui/**"

    };

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "My REST API",
                "Some custom description of API.",
                "1.0",
                "Terms of service",
                new Contact("Yakov Kontarook", "/github.com/YakovKontarook", "kontarook@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kontarook.carwashservice.carwashservice.controllers"))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder().title("Carwash service")
                        .contact(new Contact("Yakov Kontarook", "https://github.com/YakovKontarook", "kontarook@gmail.com"))
                        .description("Carwash service. Make appointments on free time slots or the next free time")
                        .build());
    }
}
