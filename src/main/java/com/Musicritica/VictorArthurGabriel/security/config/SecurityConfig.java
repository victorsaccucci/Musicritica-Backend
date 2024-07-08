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
                        .requestMatchers(HttpMethod.DELETE, "/usuario/excluir/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/usuario/buscarUsuarioDoMes").permitAll()

                        .requestMatchers(HttpMethod.GET, "/usuario/**").permitAll()//.hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/spotify/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/spotify/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/playlist/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/playlist/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/playlist/atualizar").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/playlist/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/musica/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/musica/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/comentario/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/comentario/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/avaliacao/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/avaliacao/**").permitAll()

                        .requestMatchers(HttpMethod.PUT, "/comentario/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/comentario/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/spotify/topChartsYoutube").permitAll()
                        .requestMatchers(HttpMethod.GET, "/home/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/denuncia/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/denuncia/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/denuncia/**").permitAll()

//                        .requestMatchers(HttpMethod.GET, "/denuncia/listarTodos").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/denuncia/buscar/*").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/denuncia/buscarPorData/*/*").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/denuncia/fechar/*").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/denuncia/buscarDenunciasFechadas").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/denuncia/buscarDenunciaPorUsuario/*/*").hasRole("ADMIN")


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