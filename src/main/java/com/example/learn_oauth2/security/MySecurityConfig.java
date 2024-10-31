package com.example.learn_oauth2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    @Autowired
    private MyOAuth2UserService myOAuth2UserService;

    @Autowired
    private MyOidcUserService myOidcUserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> request
                        .anyRequest().authenticated()
                )

                //表單登入(帳密登入)
                .formLogin(Customizer.withDefaults())

                //Oauth 2.0 社交登入
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(infoEndpoint -> infoEndpoint
                                .userService(myOAuth2UserService)
                                .oidcUserService(myOidcUserService)
                        )
                )

                .build();
    }
}
