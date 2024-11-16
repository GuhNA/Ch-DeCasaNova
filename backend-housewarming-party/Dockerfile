# Usar uma imagem base com JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Definir o diretório de trabalho no contêiner
WORKDIR /app

# Copiar o JAR gerado para dentro do contêiner
COPY ./target/housewarming-party-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta usada pelo Spring Boot
EXPOSE 8080

# Comando para rodar o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]
