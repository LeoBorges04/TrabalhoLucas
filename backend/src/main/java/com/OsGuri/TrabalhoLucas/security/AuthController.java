package com.OsGuri.TrabalhoLucas.security;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEnum;
import com.OsGuri.TrabalhoLucas.usuario.RoleEnum;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioEntity;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
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
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthRequestDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = jwtService.gerarToken(auth.getName());
        String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        return ResponseEntity.ok(new AuthResponseDto(token, role));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestDto data) {
        try {
            authService.register(data);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

record AuthRequestDto(String email, String senha) {}
record AuthResponseDto(String token, String role) {}
record RegisterRequestDto(
    String email, 
    String senha, 
    RoleEnum role,
    String nome,
    String cpf,
    String telefone,
    String registroConselho,
    List<EspecialidadeEnum> especialidades,
    String cargo,
    String turno,
    LocalDate dataNascimento,
    String sexo,
    String endereco,
    String convenio,
    String numeroCarteirinha
) {}
