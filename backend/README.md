# MedCore System - Backend

MedCore System é a espinha dorsal de um sistema de informação de saúde (HIS), focado no gerenciamento hospitalar. Este repositório contém a **API RESTful** que permite o cadastro, controle e visualização das operações centrais de saúde: pacientes, especialidades médicas, profissionais de saúde e o agendamento/gerenciamento de atendimentos.

Este projeto foi desenvolvido como requisito avaliativo para a disciplina de Tecnologias para Internet (Full Stack).

## 🚀 Tecnologias Utilizadas

O backend foi construído usando as seguintes tecnologias:

- **Java 21**
- **Spring Boot 3** (Web, Data JPA, Security, WebSockets)
- **PostgreSQL** (Banco de dados relacional via Supabase)
- **Autenticação JWT** (JSON Web Tokens para rotas seguras)
- **Lombok** (Para reduzir boilerplate de getters, setters, etc.)
- **Supabase CLI** (Para criação e controle de migrações locais e remotas)
- **Maven** (Gerenciamento de dependências e build)

## 🗂 Estrutura do Sistema e Funcionalidades

O sistema é focado em entidades principais (Pacientes, Especialidades, Profissionais e Atendimentos) e conta com as seguintes features avançadas:

- **Autenticação e Autorização (RBAC):** Sistema de login utilizando Spring Security e JWT. Existem diferentes papéis (Admin, Profissional, Paciente) que controlam o acesso aos endpoints e aos dados.
- **Sincronização em Tempo Real (WebSockets):** Uso de STOMP sobre WebSockets para enviar notificações ao vivo para o frontend (ex: notificar o paciente imediatamente quando a consulta for atualizada ou finalizada pelo médico).
- **Soft Delete Persistente:** Implementação de remoção lógica (`visivelPaciente`, `visivelProfissional`). A exclusão de um registro de consulta por uma das partes não afeta o histórico de visualização da outra parte.
- **Fluxo de Impressão:** Processamento de requisições preparadas para geração e impressão de relatórios e comprovantes médicos no frontend.

---

## 🛠️ Instruções de Instalação e Execução

### Pré-requisitos
- Ter o **Java 21** instalado na máquina.
- Ter o **Supabase CLI** instalado e logado.
- Ter o **Maven** instalado (ou utilizar o `./mvnw` incluso no repositório).

### Passo a passo

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/LeoBorges04/TrabalhoLucas.git
   cd TrabalhoLucas/backend
   ```

2. **Ajuste as Variáveis de Ambiente:**
   No arquivo `src/main/resources/application.properties`, você encontrará as conexões de banco de dados e configurações de JWT. Caso deseje rodar o banco localmente ou em outra instância, altere as variáveis:
   ```properties
   spring.datasource.url=jdbc:postgresql://<SEU_HOST>:5432/<NOME_DB>
   spring.datasource.username=<SEU_USER>
   spring.datasource.password=<SUA_SENHA>
   ```

3. **Inicie ou Sincronize o Banco de Dados (Supabase):**
   ```bash
   # Para enviar as migrações (tabelas) para o seu banco do Supabase configurado
   supabase db push
   ```

4. **Compile o projeto:**
   ```bash
   ./mvnw clean compile
   ```

5. **Execute a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```
   A aplicação irá iniciar por padrão na porta `8080`.

---

## 📚 Exemplos de Uso da API

*Nota: A maioria dos endpoints agora requer o envio de um token JWT válido no cabeçalho da requisição (`Authorization: Bearer <SEU_TOKEN>`).*

### 1. Autenticação

- **Login**
  - **Método:** `POST /api/auth/login`
  - **Body (JSON):**
    ```json
    {
      "email": "admin@medcore.com",
      "senha": "sua_senha"
    }
    ```

### 2. Pacientes

- **Criar Paciente**
  - **Método:** `POST /api/pacientes`
  - **Body (JSON):**
    ```json
    {
      "nome": "João da Silva",
      "cpf": "123.456.789-00",
      "data_nascimento": "1990-05-15",
      "sexo": "Masculino",
      "telefone": "(11) 98765-4321",
      "email": "joao@email.com",
      "endereco": "Rua Central, 123",
      "convenio": "Unimed",
      "numero_carteirinha": "UNM-987654321"
    }
    ```

- **Listar Pacientes Ativos**
  - **Método:** `GET /api/pacientes/ativos`

### 3. Profissionais

- **Criar Profissional**
  - **Método:** `POST /api/profissionais/{especialidadeId}`
  - **Body (JSON):**
    ```json
    {
      "nome": "Dra. Maria Oliveira",
      "registroConselho": "CRM-12345",
      "cargo": "Médica Chefe",
      "turno": "Matutino",
      "telefone": "(11) 91111-2222",
      "email": "maria@medcore.com"
    }
    ```

### 4. Atendimentos

- **Criar Atendimento**
  - **Método:** `POST /api/atendimentos`
  - **Body (JSON):**
    ```json
    {
      "pacienteId": 1,
      "profissionalId": 1,
      "dataHora": "2026-06-01T10:30:00",
      "tipo": "consulta",
      "status": "agendado",
      "diagnostico": "Aguardando avaliação",
      "observacoes": "Paciente relata dores no peito",
      "valor": 250.00
    }
    ```

- **Listar Atendimentos Filtrados por Data**
  - **Método:** `GET /api/atendimentos?data=2026-06-01`
  - **Descrição:** Retorna todos os atendimentos agendados para o dia 01/06/2026.
