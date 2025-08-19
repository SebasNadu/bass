FROM eclipse-temurin:21-jdk-jammy

RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

RUN apt-get update && apt-get upgrade && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY ./build/libs/bass-0.0.1-SNAPSHOT.jar ./app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

