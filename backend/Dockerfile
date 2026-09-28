# Build the Maven modules and generate the JWT signing key pair for this image.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY grocery-contracts/pom.xml grocery-contracts/
COPY grocery-domain/pom.xml grocery-domain/
COPY grocery-core/pom.xml grocery-core/
COPY grocery-api/pom.xml grocery-api/
COPY grocery-micronaut/pom.xml grocery-micronaut/
RUN mvn -B -q dependency:go-offline

COPY . .

RUN mvn -B -pl grocery-micronaut -am package

FROM eclipse-temurin:21-jre

COPY --from=build /workspace/grocery-micronaut/target/grocery-micronaut-1.0.0.jar /app/grocery-micronaut.jar

EXPOSE 9080 9443

ENTRYPOINT ["java", "-jar", "/app/grocery-micronaut.jar"]
