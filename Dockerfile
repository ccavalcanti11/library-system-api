# Build stage - copy pre-built JAR
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy the pre-built JAR from local build directory
COPY build/libs/*.jar app.jar

# Runtime stage
FROM amazoncorretto:21-alpine

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/app.jar app.jar

# Create non-root user for security
RUN adduser -D -s /bin/sh appuser
USER appuser

# Expose port
EXPOSE 8080

# Health check using wget instead of curl to avoid package installation
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
