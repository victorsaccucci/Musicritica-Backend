package com.Musicritica.VictorArthurGabriel.security.config;

import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import com.Musicritica.VictorArthurGabriel.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    UsuarioRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Verifica se a requisição é para o cadastro de usuário
        if (request.getRequestURI().equals("/usuario/registrar") && request.getMethod().equals("POST")) {
            // Se for, continua com o fluxo normal sem fazer autenticação
            filterChain.doFilter(request, response);
            return;
        }

        // Se não for uma requisição de cadastro de usuário, prossegue com a lógica de autenticação
        var token = this.recoverToken(request);
        if(token != null){
            var login = tokenService.validateToken(token);
            if (login != null) {
                UserDetails user = userRepository.findByEmail(login);
                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
