package org.dmitriy.Restaurant.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// тут ничего не менять кроме ссылок
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, "/product/create").permitAll()  // отдельное разрешение, надо будет убрать
                        .requestMatchers("/", "/admin/**", "/images/**", "/registration", "/product/delete/**", "/product/edit/**") // страницы на которые можнозайти без регистрации
                        .permitAll().anyRequest().authenticated() ) .
                formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/") // Указываем URL для перенаправления после успешной аутентификации
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
