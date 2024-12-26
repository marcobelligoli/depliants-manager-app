FROM maven:3.8.6-eclipse-temurin-17-alpine AS build

WORKDIR /depliants-manager-app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /depliants-manager-app

COPY --from=build /depliants-manager-app/target/depliants-manager-app.jar .

EXPOSE 8080

ENTRYPOINT java -jar ${JVM_OPTS} depliants-manager-app.jar