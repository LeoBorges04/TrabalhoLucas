package com.OsGuri.TrabalhoLucas.paciente;

import com.OsGuri.TrabalhoLucas.exception.ConflitoException;
import com.OsGuri.TrabalhoLucas.exception.RecursoNaoEncontradoException;
import com.OsGuri.TrabalhoLucas.paciente.dto.PacienteMapper;
import com.OsGuri.TrabalhoLucas.paciente.dto.PacientePatchDto;
import com.OsGuri.TrabalhoLucas.paciente.dto.PacienteRequestDto;
import com.OsGuri.TrabalhoLucas.paciente.dto.PacienteResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {
   private final PacienteRepository pacienteRepository;
   private final PacienteMapper pacienteMapper;

   public PacienteService( PacienteRepository pacienteRepository, PacienteMapper pacienteMapper){
       this.pacienteMapper =  pacienteMapper;
       this.pacienteRepository = pacienteRepository;
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

        if(dto.getData_nascimento() != null){
            paciente.setDataNascimento(dto.getData_nascimento());
        }

        if(dto.getEmail() != null){
            paciente.setEmail(dto.getEmail());
        }
        if(dto.getEndereco() != null){
            paciente.setEndereco(dto.getEndereco());
        }

        if(dto.getNome() != null){
            paciente.setNomeCompleto(dto.getNome());
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
}
