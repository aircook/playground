package com.tistory.aircook.playground.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeHttpRequests ->
                //로그인 페이지가 나오지 않게 하기 위해 아래의 anyRequest().permitAll() 부분도 추가하였습니다. 이렇게 추가하면 요청되는 모든 URL을 허용하겠다는 의미입니다.
                //authorizeRequests.anyRequest().permitAll()
                authorizeHttpRequests
                    .requestMatchers("/api1").hasRole("user")
                    .requestMatchers("/api2").hasRole("admin")
                    .anyRequest().authenticated()
            ).formLogin(formLogin
                    -> formLogin
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
            ).rememberMe(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user1").password("1234").roles("user").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance(); //deprecated
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
