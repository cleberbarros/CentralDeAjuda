# Central de Ajuda

## Sobre o Projeto
A Central de Ajuda é uma plataforma de suporte criada como resultado de um desafio de desenvolvimento. Através do sistema, os usuários podem fazer login para registrar chamados e avaliar a qualidade do atendimento prestado. Os operadores têm acesso à plataforma para auxiliar os usuários, monitorar as atualizações dos chamados em tempo real e encerrar os atendimentos. Os chamados podem ser visualizados com paginação e filtrados por assunto ou pelo número de identificação (ID).

## Tecnologias 
- **Backend:** Java 17, Spring Boot 3
- **Frontend:** Angular 17 (modo standalone)
- **Banco de Dados:** PostgreSQL 16
- **Autenticação:** OAuth2.0 com autenticação do Google
- **Comunicação em Tempo Real:** SSE (Eventos Enviados pelo Servidor)
- **Documentação da API:** Swagger

## Estrutura 
- **Backend:** Localizado no diretório `backend`, utiliza Arquitetura Limpa com as entidades Usuário, Chamado, Feedback do Chamado e Atualização do Chamado.
- **Frontend:** No diretório `frontend`, construído com Angular 17 em modo standalone.
- **Banco de Dados:** Utiliza PostgreSQL 16, com tabelas criadas automaticamente ao iniciar o serviço via Docker.

## Execução do Projeto com Docker
Este projeto está configurado para execução fácil usando Docker e Docker Compose. Certifique-se de ter Docker e Docker Compose instalados no seu sistema.

1. Clone o repositório para a sua máquina.
2. Navegue até a pasta raiz do projeto.
3. Execute o comando: `docker-compose up --build`.
    - Esse comando construirá as imagens necessárias para o backend, frontend e banco de dados, e iniciará os serviços.
4. Após a inicialização dos serviços, o frontend estará disponível em `http://localhost:4200` e o backend em `http://localhost:8080`.

## Swagger
A documentação da API está disponível através do Swagger. Você pode acessá-la em `http://localhost:8080/swagger-ui.html` para visualizar e testar as APIs disponíveis.

## Autenticação
- Para usuários regulares: A autenticação é realizada através do processo padrão de OAuth2.0 com o Google.
- Para usuários do tipo gerente: É necessário registrar diretamente o mesmo e-mail usado com o Google no banco de dados. Isso é necessário para autenticar e acessar funcionalidades específicas de gerente dentro do sistema.

## Passos do Pipeline CI/CD
O pipeline CI/CD, definido no arquivo `.github/workflows/ci.yml`, automatiza o processo de execução de testes e preparação da aplicação para deployment no GitHub Actions. O pipeline inclui os seguintes passos:

1. **Instalação de Dependências:**
   - Instala todas as dependências necessárias para as partes backend e frontend do projeto.

2. **Teste Unitário:**
   - Executa testes unitários para validar a integridade e confiabilidade do código tanto para o backend quanto para o frontend.
   - Garante que as novas alterações não quebrem funcionalidades existentes.

3. **Build:**
   - Compila o código fonte do backend em um arquivo executável `api.jar`.
   - Prepara o frontend para deployment executando o processo de build.

4. **Armazenamento:**
   - Após o build e testes bem-sucedidos o arquivo `api.jar` é disponível para download no GitHub Actions.
   - O `api.jar` pode ser baixado diretamente da seção de artefatos na página de execução do workflow no GitHub Actions.
