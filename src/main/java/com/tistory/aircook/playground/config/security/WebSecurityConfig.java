package com.tistory.aircook.playground.config.security;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@Slf4j
public class WebSecurityConfig {

    //다음과 같이 http.securityMatcher()를 이용하면 특정 요청에만 적용할 수 있다.
//    @Bean
//    @Order(1)
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//        .securityMatcher("/security/**")
//        .authorizeHttpRequests(request -> request.anyRequest().authenticated())
//        .exceptionHandling(handling ->
//            //handling.authenticationEntryPoint(new Http403ForbiddenEntryPoint())
//            handling
//                .authenticationEntryPoint((request, response, authException) ->
//                    log.warn("미인증(401), {}, {}", request.getRequestURI(), authException.getMessage())
//                )
//                .accessDeniedHandler((request, response, accessDeniedException) ->
//                    log.warn("권한부족(403), {}, {}", request.getRequestURI(), accessDeniedException.getMessage())
//                )
//        );
//        return http.build();
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(requests ->
            requests
            //anyRequest().permitAll() --> 추가하면 요청되는 모든 URL을 허용하겠다는 의미입니다.
            //.anyRequest().permitAll()
            .requestMatchers("/security/user").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/security/api1").hasRole("USER")
            .requestMatchers("/security/api2").hasRole("ADMIN")
            //AsyncController 의 end-point는 통과하기 위해서 설정
            .requestMatchers("/async/**").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/").permitAll()
            .anyRequest().authenticated())
        .formLogin(formLogin ->
            formLogin
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/", true))
        .rememberMe(Customizer.withDefaults())
        .anonymous(AbstractHttpConfigurer::disable)
        //https://tmd8633.tistory.com/79
        .exceptionHandling(handling ->
            handling
                //인증되지 않은 사용자 (401), 인증이 안된 경우
                //특정 matcher만 적용할려면 defaultAuthenticationEntryPointFor() 사용
//                .authenticationEntryPoint((request, response, authException) -> {
//                    log.warn("미인증(401), {}, {}", request.getRequestURI(), authException.getMessage());
//                })
                .defaultAuthenticationEntryPointFor((request, response, authException) ->
                    log.warn("미인증(401), {}, {}", request.getRequestURI(), authException.getMessage()),
                    new AntPathRequestMatcher("/security/**"))
                //권한 부족 처리 (403), 인증은 됐지만 권한이 없을 때
                // 특정 matcher만 적용할려면 defaultAccessDeniedHandlerFor() 사용
                .accessDeniedHandler((request, response, accessDeniedException) ->
                    log.warn("권한부족(403), {}, {}", request.getRequestURI(), accessDeniedException.getMessage())
                )
            );

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("user1").password("{noop}1234").roles("user").build());
//        return manager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance(); //deprecated
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
