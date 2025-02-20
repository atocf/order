# Usar a imagem base do OpenJDK 21
FROM openjdk:21-jdk

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR gerado pelo Maven para o diretório de trabalho
COPY target/query*.jar order-query.jar

# Expor a porta que o serviço irá utilizar
EXPOSE 8092

# Comando para iniciar o aplicativo
ENTRYPOINT ["java", "-jar", "order-query.jar"]