FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests -B

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/scripty.jar .
EXPOSE 8080
ENV PORT=8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar scripty.jar --server.port=$PORT --spring.profiles.active=prod"]
