FROM maven:3.9.8-eclipse-temurin-21 AS build
COPY src /app/src
COPY pom.xml /app
WORKDIR /app
RUN mvn clean install -U

FROM openjdk:21
COPY --from=build /app/target/prac-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
EXPOSE 18123
CMD ["java", "-jar", "app.jar"]
