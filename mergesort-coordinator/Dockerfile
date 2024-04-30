# Use the official OpenJDK image for a base.
FROM openjdk:17-slim as build

# Set the working directory in the Docker container.
WORKDIR /app

# Copy the Maven pom.xml and source code files into the Docker image.
COPY pom.xml .
COPY src src

# Install Maven dependencies and package the application.
RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests

# Use OpenJDK to run the application.
FROM openjdk:17-slim

# Copy the jar file from the build stage into the production image.
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on.
EXPOSE 8080

# Run the application.
ENTRYPOINT ["java", "-jar", "app.jar"]
