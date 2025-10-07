package com.DietIA.NextGROUP.Controller;

import com.DietIA.NextGROUP.Controller.DTO.UserDTO.JwtResponseDTO;
import com.DietIA.NextGROUP.Controller.DTO.UserDTO.LoginUserDTO;
import com.DietIA.NextGROUP.Controller.DTO.UserDTO.RegisterUserDTO;
import com.DietIA.NextGROUP.Controller.Mappers.RegisterUserMapper;
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

    @Autowired
    private RegisterUserMapper registerUserMapper;


    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody @Valid LoginUserDTO data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());


        var auth = this.authenticationManager.authenticate(usernamePassword);


        var user = (User) auth.getPrincipal();


        var token = tokenService.generateToken(user);


        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterUserDTO data) {
        if (this.userRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já está em uso.");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());

        User newUser = registerUserMapper.toEntity(data);

        this.userRepository.save(newUser);

        return ResponseEntity.ok("Usuário registrado com sucesso.");
    }
}