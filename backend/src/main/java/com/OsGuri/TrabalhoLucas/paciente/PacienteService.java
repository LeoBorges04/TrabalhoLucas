package com.OsGuri.TrabalhoLucas.paciente;

import com.OsGuri.TrabalhoLucas.exception.ConflitoException;
import com.OsGuri.TrabalhoLucas.exception.RecursoNaoEncontradoException;
import com.OsGuri.TrabalhoLucas.paciente.dto.PacienteMapper;
import com.OsGuri.TrabalhoLucas.paciente.dto.PacientePatchDto;
import com.OsGuri.TrabalhoLucas.paciente.dto.PacienteRequestDto;
import com.OsGuri.TrabalhoLucas.paciente.dto.PacienteResponseDto;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioEntity;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {
   private final PacienteRepository pacienteRepository;
   private final PacienteMapper pacienteMapper;
   private final UsuarioRepository usuarioRepository;

   public PacienteService( PacienteRepository pacienteRepository, PacienteMapper pacienteMapper, UsuarioRepository usuarioRepository){
       this.pacienteMapper =  pacienteMapper;
       this.pacienteRepository = pacienteRepository;
       this.usuarioRepository = usuarioRepository;
   }

   //Cadastrar
   @Transactional
    public PacienteResponseDto cadastrar(PacienteRequestDto dto){
       PacienteEntity paciente =this.pacienteMapper.map(dto);

       if(this.pacienteRepository.existsByCpf(paciente.getCpf())){
           throw new ConflitoException("CPF já cadastrado");
       }

       if(pacienteRepository.existsByEmail(paciente.getEmail())){
           throw new ConflitoException("Já existe paciente cadastrado com esse email");
       }

       PacienteEntity pacSalvo = this.pacienteRepository.save(paciente);

       return this.pacienteMapper.map(pacSalvo);
   }

    public PacienteResponseDto listar(Long id){
        PacienteEntity paciente = this.pacienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));// ainda tem que fazer o conflito de not found
        if(!paciente.isAtivo()){
            throw new ConflitoException("Paciente está inativo");
        }
        //map pra response
        return this.pacienteMapper.map(paciente);
   }
    public List<PacienteResponseDto> listarRegistros(){
        return this.pacienteRepository.findAll()
                .stream()
                .map(this.pacienteMapper::map)
                .toList();
    }

   //ListarAtivos
   public List<PacienteResponseDto> listarAtivos(){
        return this.pacienteRepository
                .findAllByAtivoTrue()
                .stream()
                .map(this.pacienteMapper::map)
                .toList();
   }

    //Atualiza paciente
    @Transactional
    public PacienteResponseDto atualizarParcial(Long id, PacientePatchDto dto){
        PacienteEntity paciente = pacienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
        if(!paciente.isAtivo()){

            throw new ConflitoException("Paciente está inativo");
        }

        if(dto.getCpf() != null){
            paciente.setCpf(dto.getCpf());
        }

        if(dto.getDataNascimento() != null){
            paciente.setDataNascimento(dto.getDataNascimento());
        }

        if(dto.getEmail() != null && !dto.getEmail().equals(paciente.getEmail())){
            if(pacienteRepository.existsByEmail(dto.getEmail())){
                throw new ConflitoException("Já existe paciente cadastrado com esse email");
            }
            
            Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByEmail(paciente.getEmail());
            if(usuarioOpt.isPresent()){
                UsuarioEntity usuario = usuarioOpt.get();
                usuario.setEmail(dto.getEmail());
                usuarioRepository.save(usuario);
            }
            
            paciente.setEmail(dto.getEmail());
        }
        if(dto.getEndereco() != null){
            paciente.setEndereco(dto.getEndereco());
        }

        if(dto.getNome() != null){
            paciente.setNome(dto.getNome());
        }

        if(dto.getConvenio()!= null){
            paciente.setConvenio(dto.getConvenio());
        }

        if(dto.getNumeroCarteirinha()!= null){
            paciente.setNumeroCarteirinha(dto.getNumeroCarteirinha());
        }

        if(dto.getSexo()!= null){
            paciente.setSexo(dto.getSexo());
        }

        if(dto.getTelefone() != null){
            paciente.setTelefone(dto.getTelefone());
        }
        pacienteRepository.save(paciente);
        return pacienteMapper.map(paciente);

    }

    //Reativa paciente
    @Transactional
    public PacienteResponseDto reativar(Long id){
        PacienteEntity paciente = pacienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
        if(paciente.isAtivo()){
            throw new ConflitoException("Paciente já está ativo");
        }
        paciente.setAtivo(true);
        return pacienteMapper.map(paciente);

    }
    //Desativa paciente (soft delete)
    @Transactional
    public void deletar(Long id){
        PacienteEntity paciente = pacienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
        if(!paciente.isAtivo()){
            throw new ConflitoException("Paciente já está inativo");
        }
        paciente.setAtivo(false);
    }

    public PacienteResponseDto obterMeuPerfil() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }
        String email = auth.getName();
        PacienteEntity paciente = pacienteRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente logado não encontrado"));
        return pacienteMapper.map(paciente);
    }
}
