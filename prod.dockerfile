FROM eclipse-temurin:21-jdk-jammy

RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

WORKDIR /app

COPY ./build/libs/bass-0.0.1-SNAPSHOT.jar ./app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=5s --start-period=15s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT exec java \                # Run Java as the main container process. "exec" replaces the shell
                                      # with the Java process (PID 1) so signals (like SIGTERM) are delivered
                                      # correctly for clean shutdown in Docker/Kubernetes.

  -Xms512m \                          # Set the **initial Java heap size** to 512 MB.
                                      # The JVM will start with this much memory allocated for the heap.

  -Xmx1024m \                         # Set the **maximum Java heap size** to 1024 MB (1 GB).
                                      # Prevents the JVM from consuming more than this much heap memory.

  -XX:MaxMetaspaceSize=256m \         # Limit the **Metaspace** (where JVM stores class metadata) to 256 MB.
                                      # Prevents class metadata from growing without bounds.

  -XX:+UseG1GC \                      # Use the **G1 Garbage Collector**, good for low-latency,
                                      # large heap applications. It's the modern default in new JVMs.

  -XX:+HeapDumpOnOutOfMemoryError \   # Instructs the JVM to generate a **heap dump file** if
                                      # an OutOfMemoryError occurs (useful for debugging).

  -XX:HeapDumpPath=/app/heapdump.hprof \
                                      # Location where the heap dump file will be written when OOM happens.

  -jar /app/app.jar                   # Run the JAR file located at /app/app.jar.
                                      # This is your Spring Boot (or other Java) application.
