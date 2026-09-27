# Build the Maven modules and generate the JWT signing key pair for this image.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY grocery-contracts/pom.xml grocery-contracts/
COPY grocery-domain/pom.xml grocery-domain/
COPY grocery-core/pom.xml grocery-core/
COPY grocery-api/pom.xml grocery-api/
RUN mvn -B -q dependency:go-offline

COPY . .

# The public key is packaged on the application classpath so MicroProfile JWT can verify tokens.
RUN openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out /workspace/privateKey.pem \
 && openssl rsa -in /workspace/privateKey.pem -pubout -out /workspace/publicKey.pem \
 && cp /workspace/publicKey.pem grocery-api/src/main/resources/publicKey.pem

RUN mvn -B package

FROM icr.io/appcafe/open-liberty:full-java21-openj9-ubi-minimal

COPY --chown=1001:0 --from=build /workspace/grocery-api/src/main/liberty/config/server.xml /config/server.xml
COPY --chown=1001:0 --from=build /workspace/grocery-api/target/grocery-api.war /config/apps/grocery-api.war
COPY --chown=1001:0 --from=build /workspace/grocery-api/target/jdbc/postgresql.jar /opt/ol/wlp/usr/shared/resources/postgresql.jar
COPY --chown=1001:0 --from=build /workspace/privateKey.pem /config/privateKey.pem

RUN configure.sh

EXPOSE 9080 9443
