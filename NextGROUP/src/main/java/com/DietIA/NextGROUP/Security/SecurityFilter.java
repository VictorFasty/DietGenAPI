package com.DietIA.NextGROUP.security;

import com.DietIA.NextGROUP.Repository.UserRepository;
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

@Component // Anotação para que o Spring gerencie este componente
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private com.DietIA.NextGROUP.security.TokenService tokenService; // Nosso serviço que sabe validar o token

    @Autowired
    private UserRepository userRepository; // Repositório para buscar o usuário no banco

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Tenta recuperar o token do cabeçalho da requisição
        var token = this.recoverToken(request);

        // 2. Se um token foi encontrado...
        if (token != null) {
            // 3. Valida o token e extrai o email (subject) de dentro dele
            var email = tokenService.validateToken(token);
            // 4. Com o email, busca os dados do usuário no banco de dados
            UserDetails user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

            // 5. Cria um objeto de autenticação para o Spring Security
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            // 6. Salva o objeto de autenticação no contexto de segurança. Isso "informa" ao Spring que o usuário está logado para esta requisição.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 7. Continua a cadeia de filtros. Se não havia token, a requisição segue para ser barrada ou liberada pelo SecurityConfig.
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair o token do cabeçalho "Authorization"
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        // O token vem no formato "Bearer <token>", então removemos o "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}