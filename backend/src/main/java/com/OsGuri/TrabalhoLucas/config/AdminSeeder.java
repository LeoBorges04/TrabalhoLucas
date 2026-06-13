package com.OsGuri.TrabalhoLucas.config;

import com.OsGuri.TrabalhoLucas.usuario.RoleEnum;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioEntity;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.findByEmail("admin@medcore.com").isEmpty()) {
                UsuarioEntity admin = new UsuarioEntity();
                admin.setEmail("admin@medcore.com");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setRole(RoleEnum.ADMIN);
                repository.save(admin);
                System.out.println(">>> CONTA ADMIN GERADA COM SUCESSO: admin@medcore.com / admin123 <<<");
            }
        };
    }
}
