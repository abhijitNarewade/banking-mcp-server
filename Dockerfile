FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY target/banking-mcp-server-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
