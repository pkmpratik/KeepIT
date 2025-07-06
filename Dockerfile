FROM maven:3.9.7 AS build_stage
WORKDIR "/app"
COPY .mvn .mvn
COPY mvnw mvnw
RUN chmod +x mvnw
COPY pom.xml pom.xml
COPY src src
RUN ./mvnw package -DskipTests

FROM openjdk:21
WORKDIR "/app"
COPY --from=build_stage /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]