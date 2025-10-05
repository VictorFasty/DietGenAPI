package com.DietIA.NextGROUP.security;

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

@Configuration // Indica que esta é uma classe de configuração do Spring
@EnableWebSecurity // Habilita a segurança web do Spring
public class SecurityConfig {

    @Autowired
    private com.DietIA.NextGROUP.security.SecurityFilter securityFilter; // Nosso filtro customizado

    @Bean // Expõe o resultado deste método como um Bean gerenciado pelo Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Desabilita a proteção CSRF. Como usaremos JWT, que é stateless, não precisamos disso.
                .csrf(csrf -> csrf.disable())

                // 2. Configura a política de gerenciamento de sessão como STATELESS. O servidor не guardará estado da sessão do usuário.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configura as regras de autorização para as requisições HTTP.
                .authorizeHttpRequests(authorize -> authorize
                        // Permite que qualquer um acesse os endpoints de registro e login.
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        // Para todas as outras requisições, o usuário precisa estar autenticado.
                        .anyRequest().authenticated()
                )

                // 4. Adiciona nosso filtro (SecurityFilter) para ser executado ANTES do filtro padrão de autenticação do Spring.
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    // Bean para que o Spring consiga injetar o AuthenticationManager em outras classes (como no nosso AuthController).
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    // Bean que define o algoritmo de criptografia de senhas. Usamos o BCrypt, que é o padrão e muito seguro.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}