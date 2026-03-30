FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/qma-service-0.0.1-SNAPSHOT.jar qma-service.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "qma-service.jar"]