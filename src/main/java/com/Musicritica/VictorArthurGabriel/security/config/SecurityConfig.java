package com.Musicritica.VictorArthurGabriel.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/usuario/registrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/esqueceuSenha").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/redefinirSenha/{token}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/usuario/atualizar").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/usuario/excluir/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuario/**").permitAll()//.hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/spotify/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/spotify/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/playlist/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/playlist/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/musica/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comentario/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/comentario/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/comentario/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/comentario/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/spotify/topChartsYoutube").permitAll()
                        .requestMatchers(HttpMethod.GET, "/home/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}