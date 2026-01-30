<!-- Substitui o README atual por uma versão no estilo fornecido pelo usuário, mas adaptada ao projeto de autenticação (authenticator-api) -->
# 🔐 Authenticator API — Sistema de Autenticação e Gerenciamento de Usuários

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

> API leve e segura para autenticação, autorização e gerenciamento de usuários — ideal para integrar com aplicações web e mobile.

---

## 🎯 O Problema
Muitas aplicações precisam de um serviço centralizado de autenticação: tokens seguros, gerenciamento de permissões, recuperação de senha e proteção contra ataques de força bruta. Implementar isso de forma consistente e segura pode ser custoso e sujeito a erros.

## 💡 A Solução
`authenticator-api` oferece um sistema pronto para autenticação stateless (JWT), gerenciamento de usuários/roles, recuperação de senha com e-mail e proteção contra abuso (rate limiting e lockout). Fácil de integrar via REST e documentado com OpenAPI/Swagger.

---

## 🔥 Principais funcionalidades

### 🔐 Segurança & Autenticação
- Tokens JWT assinados por `SPRING_JWT_SECRET`.
- `tokenVersion` para invalidar tokens de forma controlada.
- Proteção contra brute-force com lockout temporário (configurável via `application.yml`).
- RBAC: roles configuráveis (ex.: `ADMIN`, `USER`, `SUPPORT`, etc.).
- Senhas armazenadas com hashing seguro (BCrypt).

### 🧾 Gerenciamento de Usuários
- CRUD de usuários e permissões.
- Campos de perfil (nome, username, email, status, etc.).
- Log de eventos (criação, atualização, desativação).

### 📧 Recuperação de Senha & Notificações
- Tokens temporários de recuperação com expiração configurável.
- Integração com provedores de e-mail (Resend) para envio de templates HTML.

### 🛡 Resiliência & Abuse Protection
- Rate limiting (Bucket4j) e cache (Caffeine).
- Bloqueio por tentativas excessivas (configurável).
- Asynchronous email dispatch e background jobs quando aplicável.

### 📚 Documentação & Observabilidade
- OpenAPI / Swagger UI (springdoc).
- Logs e mensagens claras para debug.

---

## 🛠️ Stack Tecnológica
- Kotlin 2.2.x
- Spring Boot 4.0.x
- Spring Security
- Spring Data JPA (Hibernate)
- PostgreSQL
- JWT (jjwt)
- springdoc-openapi (Swagger UI)
- Bucket4j / Caffeine
- Resend Java SDK
- Gradle (wrapper)
- Docker & docker-compose

---

## 🚀 Como rodar (rápido)

### Pré-requisitos
- Java 17+
- Docker (recomendado)
- Gradle (use o wrapper `./gradlew`)

### 1) Clone
```bash
git clone https://github.com/GBLins14/authenticator-api.git
cd authenticator-api
```

### 2) Configure variáveis de ambiente
Copie e preencha o `.env` a partir do template:
```bash
cp .env.example .env
# então edite .env para adicionar SPRING_JWT_SECRET, SPRING_DATASOURCE_*, RESEND_API_KEY, etc.
```

> A aplicação carrega `.env` automaticamente (Dotenv). Em produção, use secret managers.

### 3A) Rodar com Docker Compose (recomendado)
```bash
docker compose build
docker compose up -d

# logs da API
docker compose logs -f api

# parar e remover
docker compose down -v
```

### 3B) Rodar localmente (Gradle)
```bash
./gradlew bootRun
```
Acesse: http://localhost:8080

---

## ⚙️ Variáveis de ambiente
Todas as variáveis abaixo são **essenciais** e devem ser configuradas. Extraídas de `src/main/resources/application.yml`:

### Resend (Notificações por E-mail)
- RESEND_API_KEY — chave da API Resend para envio de e-mails

### App (Configurações de Aplicação)
- APP_FRONTEND_URL — URL do front-end (usada em links de e-mail)
- APP_SWAGGER_URL — URL pública da documentação Swagger
- APP_MAX_REQUESTS_PER_MINUTES — limite de requisições por minuto (rate limiting)

### App > Sign (Validações de Cadastro)
- APP_MIN_FULLNAME_LENGTH — comprimento mínimo do nome completo
- APP_MIN_USERNAME_LENGTH — comprimento mínimo do username
- APP_MAX_USERNAME_LENGTH — comprimento máximo do username
- APP_MIN_PASSWORD_LENGTH — comprimento mínimo da senha
- APP_MAX_PASSWORD_LENGTH — comprimento máximo da senha
- APP_MAX_ATTEMPTS — número máximo de tentativas de login antes de lockout
- APP_LOCKOUT_MINUTES — tempo de bloqueio em minutos

### App > Password Recovery (Recuperação de Senha)
- APP_TOKEN_EXPIRATION_MINUTES — expiração do token de recuperação de senha (minutos)

### JWT (Autenticação)
- SPRING_JWT_SECRET — segredo para assinar e validar JWTs
- SPRING_JWT_EXPIRATION_DAYS — validade do token JWT (dias)

### Spring Datasource (PostgreSQL)
- SPRING_DATASOURCE_URL — JDBC URL do banco (ex.: jdbc:postgresql://host:5432/dbname)
- SPRING_DATASOURCE_USERNAME — usuário do banco
- SPRING_DATASOURCE_PASSWORD — senha do banco
- SPRING_DATASOURCE_DRIVER — driver JDBC (padrão: org.postgresql.Driver)

> Nunca comite `.env` com segredos. Use variáveis de ambiente do sistema ou secret managers em produção.

---


## 🧪 Testes

Executar suíte de testes:
```bash
./gradlew test
```

Dica: para testes de integração, use Testcontainers para orquestrar um PostgreSQL isolado.

---

## 🧭 Documentação (Swagger)
- OpenAPI JSON: `/v3/api-docs`
- Swagger UI: `/swagger-ui/index.html`

Ex.: `http://localhost:8080/swagger-ui/index.html`

---

## 🛠️ Debug & Problemas comuns
- "Arquivo .env não encontrado": crie o `.env` ou exporte variáveis no ambiente.
- Erro de conexão com Postgres: verifique `SPRING_DATASOURCE_URL` e se o container `db` está ativo.
- Porta 8080 ocupada: altere `server.port` em `application.yml`.
- E-mail não enviado: verifique `RESEND_API_KEY`.
- JWT inválido/expirado: verifique `SPRING_JWT_SECRET`.

---


## 📦 Licença
MIT — ver arquivo `LICENSE`.

---

<p align="center"><sub>Desenvolvido por Gabriel Lins</sub></p>
