# Use the official OpenJDK base image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file into the container
COPY target/api-transferencia-0.0.1-SNAPSHOT.jar /app/app.jar

# Specify the command to run on container startup
CMD ["java", "-jar", "/app/app.jar"]
