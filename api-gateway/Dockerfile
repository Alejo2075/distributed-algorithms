# Use the official OpenJDK image for a Java base
FROM openjdk:17-slim as build

# Set the working directory inside the Docker image
WORKDIR /app

# Copy Maven's pom file and source code
COPY pom.xml .
COPY src src

# Install Maven, build the application and skip unit tests
RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests

# Start a new stage to create a lighter final image
FROM openjdk:17-slim

# Copy the built jar file from the build stage to this new stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the Gateway runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
