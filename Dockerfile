FROM maven:latest AS maven_build
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package

FROM openjdk:11.0.7-jre
WORKDIR /app
COPY --from=maven_build /build/target/accenture-one-pre-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "accenture-one-pre-0.0.1-SNAPSHOT.jar"]