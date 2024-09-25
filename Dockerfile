FROM eclipse-temurin:17-jre-alpine

# Crie um diretório para o app
WORKDIR /work/app

COPY target/quarkus-app/lib/ /work/app/lib/
COPY target/quarkus-app/*.jar /work/app/
COPY target/quarkus-app/app/ /work/app/app/
COPY target/quarkus-app/quarkus/ /work/app/quarkus/
COPY target/quarkus-app/quarkus-run.jar /work/app/quarkus-run.jar

EXPOSE 8080

# Execute o JAR
CMD ["java", "-jar", "/work/app/quarkus-run.jar"]
