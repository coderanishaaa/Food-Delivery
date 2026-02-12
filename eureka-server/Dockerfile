FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the built jar from the target directory
COPY target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java","-jar","/app/app.jar"]

