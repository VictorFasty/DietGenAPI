package com.DietIA.NextGROUP.Controller;

import com.DietIA.NextGROUP.Controller.DTO.UserDTO.JwtResponseDTO;
import com.DietIA.NextGROUP.Controller.DTO.UserDTO.LoginUserDTO;
import com.DietIA.NextGROUP.Controller.DTO.UserDTO.RegisterUserDTO;
import com.DietIA.NextGROUP.Model.User;
import com.DietIA.NextGROUP.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.DietIA.NextGROUP.security.TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody @Valid LoginUserDTO data) {
        // 1. Cria um "pacote" com email e senha para o Spring Security processar.
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        // 2. O AuthenticationManager tenta autenticar. Se as credenciais estiverem erradas, ele lança uma exceção que o Spring trata automaticamente (retornando um erro 403 Forbidden).
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Se a autenticação foi bem-sucedida, pega o objeto do usuário que foi autenticado.
        var user = (User) auth.getPrincipal();

        // 4. Gera o token JWT para este usuário.
        var token = tokenService.generateToken(user);

        // 5. Retorna o token em um DTO.
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterUserDTO data) {
        // 1. Verifica se já existe um usuário com o mesmo email.
        if (this.userRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já está em uso.");
        }

        // 2. Criptografa a senha recebida. NUNCA salve senhas em texto puro!
        String encryptedPassword = passwordEncoder.encode(data.password());

        // 3. Cria uma nova entidade User com os dados recebidos.
        User newUser = new User(data.name(), data.email(), encryptedPassword);

        // 4. Salva o novo usuário no banco de dados.
        this.userRepository.save(newUser);

        // 5. Retorna uma resposta de sucesso.
        return ResponseEntity.ok("Usuário registrado com sucesso.");
    }
}