package com.example.assados;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Bean do BCrypt — usado no LoginService
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Desativa CSRF (necessário para APIs REST com frontend separado)
                .csrf(AbstractHttpConfigurer::disable)

                // Define quais rotas são públicas e quais precisam de autenticação
                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas — login e cadastro não precisam de token
                        .requestMatchers("/api/auth/**").permitAll()
                        // Todas as outras rotas precisam estar autenticado
                        .anyRequest().permitAll() // troque para .authenticated() quando implementar JWT
                )

                // Desativa o login padrão do Spring (tela branca)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}