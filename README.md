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

## 🚀 Como rodar

### Pré-requisitos
- Java 17+
- Docker (recomendado para produção)
- Gradle (use o wrapper `./gradlew`)
- PostgreSQL (ou use Docker)

### 1) Clone
```bash
git clone https://github.com/GBLins14/authenticator-api.git
cd authenticator-api/auth
```

### 2A) Rodar com Docker Compose (recomendado)
```bash
# Build e inicie containers
docker compose build
docker compose up -d

# Verifique logs
docker compose logs -f api

# Parar
docker compose down
```

**Nota:** Docker Compose roda com banco local. Para produção, veja [Configuração em Produção](#-configuração-em-produção).

### 2B) Rodar localmente (Gradle)
```bash
# Inicie PostgreSQL separadamente (Docker ou local)
docker run -d \
  --name postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=authenticator_db \
  -p 5432:5432 \
  postgres:15-alpine

# Configure variáveis de ambiente
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/authenticator_db"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="postgres"
export SPRING_JWT_SECRET="aB3cDeFgHiJkLmNoPqRsT9UvWxYz1234567890aBcDeFgHiJkLmNoPqRsT"
export SPRING_JWT_EXPIRATION_DAYS="7"
export APP_FRONTEND_URL="http://localhost:3000"
export APP_SWAGGER_URL="http://localhost:8080"

# Rode a aplicação
./gradlew bootRun
```

Acesse: http://localhost:8080/swagger-ui/index.html

---

## ⚙️ Variáveis de Ambiente

Todas as variáveis abaixo são **essenciais** e devem ser configuradas no seu ambiente de deploy. 

### 🔑 Essenciais (Sempre Requeridas)

#### JWT (Autenticação)
- `SPRING_JWT_SECRET` — segredo para assinar/validar JWTs (mín. 256 bits)
- `SPRING_JWT_EXPIRATION_DAYS` — validade do token JWT em dias (ex: 7)

#### Banco de Dados (PostgreSQL)
- `SPRING_DATASOURCE_URL` — JDBC URL (ex: `jdbc:postgresql://host:5432/dbname`)
- `SPRING_DATASOURCE_USERNAME` — usuário do banco
- `SPRING_DATASOURCE_PASSWORD` — senha do banco
- `SPRING_DATASOURCE_DRIVER` — driver JDBC (padrão: `org.postgresql.Driver`)

### 📧 Email & Notificações
- `RESEND_API_KEY` — chave da API Resend para envio de e-mails

### 🌐 URLs da Aplicação
- `APP_FRONTEND_URL` — URL do front-end (usada em links de e-mail, ex: `https://seu-app.com`)
- `APP_SWAGGER_URL` — URL pública da documentação Swagger (ex: `https://api.seu-app.com`)

### 🔒 Validações de Cadastro
- `APP_MIN_FULLNAME_LENGTH` — mínimo de caracteres para nome completo (padrão: 7)
- `APP_MIN_USERNAME_LENGTH` — mínimo de caracteres para username (padrão: 4)
- `APP_MAX_USERNAME_LENGTH` — máximo de caracteres para username (padrão: 20)
- `APP_MIN_PASSWORD_LENGTH` — mínimo de caracteres para senha (padrão: 6)
- `APP_MAX_PASSWORD_LENGTH` — máximo de caracteres para senha (padrão: 30)
- `APP_MAX_ATTEMPTS` — tentativas de login antes de lockout (padrão: 5)
- `APP_LOCKOUT_MINUTES` — tempo de bloqueio em minutos (padrão: 5)
- `APP_TOKEN_EXPIRATION_MINUTES` — expiração do token de recuperação de senha (padrão: 5)
- `APP_MAX_REQUESTS_PER_MINUTES` — limite de requisições por minuto (padrão: 40)

---

## 📦 Configuração em Produção

### 🚂 Railway

1. **Crie um projeto no Railway**
   ```
   https://railway.app/dashboard
   ```

2. **Adicione PostgreSQL**
   - Clique em "Add Service" → PostgreSQL
   - Railway fornecerá: `DATABASE_URL`

3. **Configure Variáveis de Ambiente**
   No painel Railway, vá para "Variables" e adicione:
   
   ```
   SPRING_DATASOURCE_URL=postgresql://user:password@host:5432/dbname
   SPRING_DATASOURCE_USERNAME=seu_usuario
   SPRING_DATASOURCE_PASSWORD=sua_senha
   SPRING_DATASOURCE_DRIVER=org.postgresql.Driver
   
   SPRING_JWT_SECRET=aB3cDeFgHiJkLmNoPqRsT9UvWxYz1234567890aBcDeFgHiJkLmNoPqRsT
   SPRING_JWT_EXPIRATION_DAYS=7
   
   APP_FRONTEND_URL=https://seu-frontend.railway.app
   APP_SWAGGER_URL=https://seu-backend.railway.app
   
   RESEND_API_KEY=re_seu_api_key_aqui
   
   APP_MIN_FULLNAME_LENGTH=7
   APP_MIN_USERNAME_LENGTH=4
   APP_MAX_USERNAME_LENGTH=20
   APP_MIN_PASSWORD_LENGTH=6
   APP_MAX_PASSWORD_LENGTH=30
   APP_MAX_ATTEMPTS=5
   APP_LOCKOUT_MINUTES=5
   APP_TOKEN_EXPIRATION_MINUTES=5
   APP_MAX_REQUESTS_PER_MINUTES=40
   ```

4. **Deploy**
   - Conecte seu repositório Git
   - Railway faz build e deploy automaticamente

### 🐳 Docker (Seu Servidor)

```bash
# Faça login no Docker Registry
docker login

# Build
docker build -t seu-usuario/authenticator-api .

# Push
docker push seu-usuario/authenticator-api

# Rode no servidor
docker run -d \
  --name authenticator-api \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://seu-db:5432/authenticator_db" \
  -e SPRING_DATASOURCE_USERNAME="postgres" \
  -e SPRING_DATASOURCE_PASSWORD="sua-senha-segura" \
  -e SPRING_JWT_SECRET="sua-chave-jwt-segura" \
  -e RESEND_API_KEY="sua-api-key" \
  seu-usuario/authenticator-api
```

### ☸️ Kubernetes

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: authenticator-config
data:
  APP_FRONTEND_URL: "https://seu-app.com"
  APP_SWAGGER_URL: "https://api.seu-app.com"
  SPRING_DATASOURCE_DRIVER: "org.postgresql.Driver"
---
apiVersion: v1
kind: Secret
metadata:
  name: authenticator-secrets
type: Opaque
stringData:
  SPRING_DATASOURCE_URL: "jdbc:postgresql://postgres:5432/authenticator_db"
  SPRING_DATASOURCE_USERNAME: "postgres"
  SPRING_DATASOURCE_PASSWORD: "sua-senha-segura"
  SPRING_JWT_SECRET: "sua-chave-jwt-segura"
  RESEND_API_KEY: "sua-api-key"
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: authenticator-api
spec:
  replicas: 2
  template:
    spec:
      containers:
      - name: api
        image: seu-usuario/authenticator-api:latest
        ports:
        - containerPort: 8080
        envFrom:
        - configMapRef:
            name: authenticator-config
        - secretRef:
            name: authenticator-secrets
```

---

## 🧪 Testes

```bash
./gradlew test
```

---

## 🧭 Documentação (Swagger)
- **OpenAPI JSON:** `/v3/api-docs`
- **Swagger UI:** `/swagger-ui/index.html`

Exemplo: `https://seu-api.com/swagger-ui/index.html`

---

## 🛠️ Debug & Problemas Comuns

### "Conexão recusada ao banco"
- Verifique `SPRING_DATASOURCE_URL`
- Garanta que PostgreSQL está rodando
- Em Docker, use o nome do serviço como host

### "JWT inválido/expirado"
- Verifique se `SPRING_JWT_SECRET` é igual em dev e prod
- Secret deve ter mín. 256 bits

### "E-mail não enviado"
- Verifique `RESEND_API_KEY`
- Confirme que a chave é válida

### "Porta 8080 ocupada"
- Mude `server.port` em `application-prod.yml`
- Ou mate o processo: `kill -9 $(lsof -t -i:8080)`

---

## 📝 Estrutura do Projeto

```
auth/
├── src/
│   ├── main/
│   │   ├── kotlin/com/authenticator/authenticator_api/
│   │   │   ├── controllers/      # Endpoints REST
│   │   │   ├── services/         # Lógica de negócio
│   │   │   ├── repositories/     # Data access
│   │   │   ├── security/         # JWT, filters
│   │   │   └── configs/          # Configurações
│   │   └── resources/
│   │       ├── application.yml   # Config base
│   │       ├── application-dev.yml
│   │       └── application-prod.yml
│   └── test/                     # Testes
├── docker-compose.yml             # Dev/local
├── Dockerfile                      # Produção
└── README.md
```

---

## 📦 Licença
MIT — ver arquivo `LICENSE`.

---

<p align="center"><sub>Desenvolvido por Gabriel Lins</sub></p>
