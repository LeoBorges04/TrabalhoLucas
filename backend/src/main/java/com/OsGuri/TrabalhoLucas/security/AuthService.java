package com.OsGuri.TrabalhoLucas.security;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEnum;
import com.OsGuri.TrabalhoLucas.paciente.PacienteEntity;
import com.OsGuri.TrabalhoLucas.paciente.PacienteRepository;
import com.OsGuri.TrabalhoLucas.profissional.ProfissionalEntity;
import com.OsGuri.TrabalhoLucas.profissional.ProfissionalRepository;
import com.OsGuri.TrabalhoLucas.usuario.RoleEnum;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioEntity;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository, ProfissionalRepository profissionalRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterRequestDto data) {
        if (usuarioRepository.findByEmail(data.email()).isPresent()) {
            throw new RuntimeException("E-mail já está em uso.");
        }

        UsuarioEntity newUser = new UsuarioEntity();
        newUser.setEmail(data.email());
        newUser.setSenha(passwordEncoder.encode(data.senha()));
        newUser.setRole(data.role());

        usuarioRepository.save(newUser);

        if (data.role() == RoleEnum.PACIENTE) {
            PacienteEntity paciente = new PacienteEntity();
            paciente.setNome(data.nome());
            paciente.setCpf(data.cpf());
            paciente.setTelefone(data.telefone());
            paciente.setEmail(data.email());
            paciente.setDataNascimento(data.dataNascimento());
            paciente.setSexo(data.sexo() != null ? data.sexo() : "Não informado");
            paciente.setEndereco(data.endereco() != null ? data.endereco() : "Não informado");
            paciente.setConvenio(data.convenio() != null ? data.convenio() : "Nenhum");
            paciente.setNumeroCarteirinha(data.numeroCarteirinha() != null ? data.numeroCarteirinha() : "0000000000");
            paciente.setAtivo(true);
            pacienteRepository.save(paciente);
        } else if (data.role() == RoleEnum.MEDICO) {
            ProfissionalEntity profissional = new ProfissionalEntity();
            profissional.setNome(data.nome());
            profissional.setRegistroConselho(data.registroConselho());
            profissional.setEspecialidades(data.especialidades());
            profissional.setCargo(data.cargo() != null ? data.cargo() : "Médico");
            profissional.setTurno(data.turno() != null ? data.turno() : "Integral");
            profissional.setTelefone(data.telefone());
            profissional.setEmail(data.email());
            profissional.setAtivo(true);
            profissionalRepository.save(profissional);
        } else if (data.role() == RoleEnum.ADMIN) {
            // Pode implementar logica caso admin tenha uma tabela própria.
        }
    }
}
