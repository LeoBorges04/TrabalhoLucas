# MedCore System - Frontend

Este é o frontend do **MedCore System**, um sistema de gestão de saúde (HIS) construído com uma arquitetura SPA (Single Page Application) moderna, adotando o estilo visual **Glassmorphism** para uma interface fluida, responsiva e elegante.

## 🚀 Tecnologias Utilizadas

- **React 19**
- **Vite** (Build tool e dev server super rápido)
- **React Router DOM v7** (Roteamento client-side)
- **Axios** (Requisições HTTP com interceptors para injeção de tokens JWT)
- **StompJS & SockJS** (Comunicação em tempo real via WebSockets)
- **Vanilla CSS** (Estilização com variáveis e design system focado em Glassmorphism, sem frameworks adicionais)

## ✨ Funcionalidades Principais

- **Design System Glassmorphism:** Interface limpa, com transparências, desfoques de fundo e tons profundos de azul.
- **Autenticação e Perfis de Acesso (RBAC):** Login seguro com JWT. O sistema adapta as rotas, telas e permissões com base no perfil do usuário (Administrador, Médico ou Paciente).
- **Ciclo de Vida de Consultas:** Fluxo completo onde um Paciente solicita, o Admin/Médico agenda e, posteriormente, finaliza a consulta com anotações clínicas.
- **Sincronização em Tempo Real (WebSockets):** Atualizações instantâneas de status de consultas. Quando o médico finaliza um atendimento, o paciente recebe a atualização na sua tela em tempo real.
- **Soft Delete e Visibilidade Dinâmica:** Exclusão lógica de registros (como consultas) garantindo que a deleção por parte de um médico não quebre o histórico de visualização do paciente.
- **Fluxo de Impressão:** Geração e impressão de dados e relatórios (como receituários) diretamente do sistema.

## 🛠️ Como Executar o Projeto

### Pré-requisitos
- **Node.js** instalado.
- **NPM** ou **Yarn**.

### Passos

1. **Clone o repositório e acesse a pasta do frontend:**
   ```bash
   git clone https://github.com/LeoBorges04/TrabalhoLucas.git
   cd TrabalhoLucas/frontend
   ```

2. **Instale as dependências:**
   ```bash
   npm install
   ```

3. **Inicie o servidor de desenvolvimento:**
   ```bash
   npm run dev
   ```

O aplicativo estará rodando por padrão em `http://localhost:5173` ou na porta informada no terminal. Certifique-se de que a API (backend) também esteja rodando para que as chamadas de rede e autenticação funcionem corretamente.
