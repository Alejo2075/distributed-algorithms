# Use the official OpenJDK image for a base.
FROM openjdk:17-slim as build

# Set the working directory in the Docker container.
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy only the Maven pom.xml first to fetch dependencies
COPY pom.xml .

# Download dependencies as a separate layer to leverage Docker cache
RUN mvn dependency:go-offline

# Copy the source code files into the Docker image.
COPY src src

# Package the application without running tests to speed up the build
RUN mvn clean package -DskipTests

# Use OpenJDK to run the application.
FROM openjdk:17-slim

# Copy the jar file from the build stage into the production image.
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on.
EXPOSE 8081

# Run the application.
ENTRYPOINT ["java", "-jar", "app.jar"]
