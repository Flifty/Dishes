FROM eclipse-temurin:21-jdk
WORKDIR /app
ENV PORT 8080
EXPOSE 8080
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
