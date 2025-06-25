FROM maven:3.9.7 AS build_stage
WORKDIR "/app"
COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml
COPY src src
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

FROM openjdk:21
ENV MONGO_URI="mongodb+srv://pratik:pratik@keepitcluster.5ldpmlp.mongodb.net/?retryWrites=true&w=majority&appName=KeepITCluster"
WORKDIR "/app"
COPY --from=build_stage /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]