FROM eclipse-temurin:21-jdk-jammy

RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

WORKDIR /app

COPY ./build/libs/bass-0.0.1-SNAPSHOT.jar ./app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=5s --start-period=15s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java",
           "-Xms512m",
           "-Xmx1024m",
           "-XX:MaxMetaspaceSize=256m",
           "-XX:+UseG1GC",
           "-XX:+HeapDumpOnOutOfMemoryError",
           "-XX:HeapDumpPath=/app/heapdump.hprof",
           "-jar", "/app/app.jar"]
