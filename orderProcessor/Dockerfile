# Usar a imagem base do OpenJDK 21
FROM openjdk:21-jdk

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR gerado pelo Maven para o diretório de trabalho
COPY target/processor*.jar order-processor.jar

# Expor a porta que o serviço irá utilizar
EXPOSE 8090

# Comando para iniciar o aplicativo
ENTRYPOINT ["java", "-jar", "order-processor.jar"]