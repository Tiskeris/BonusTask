#no idea if works docker refused to work :)
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN ./mvnw package -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
