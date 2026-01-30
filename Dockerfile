# --- Estágio de Build (IGUAL AO SEU) ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# OTIMIZAÇÃO DE CACHE
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Arruma permissões do Windows
RUN dos2unix gradlew || true
RUN chmod +x gradlew

# Baixa dependências
RUN ./gradlew dependencies --no-daemon || true

# Copia código e builda
COPY src src
RUN ./gradlew clean build -x test --no-daemon

# --- Estágio Final (CORRIGIDO) ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Instala libs e TIMEZONE
RUN apk add --no-cache libgcc gcompat tzdata
ENV TZ=America/Recife

# 1. Primeiro copiamos a pasta de libs do estágio 'builder' para uma pasta temporária aqui
COPY --from=builder /app/build/libs/ /tmp/libs/

# 2. Agora rodamos o seu comando de busca inteligente na pasta temporária
RUN cp $(find /tmp/libs/ -name "*.jar" ! -name "*-plain.jar") app.jar && \
    rm -rf /tmp/libs/

# Configurações Finais
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]