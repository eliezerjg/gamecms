FROM openjdk:21

WORKDIR /app
COPY target/*.jar platform-backend.jar
EXPOSE 8777:8777

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/platform-backend.jar"]
