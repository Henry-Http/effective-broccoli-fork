FROM maven:3.8.4-openjdk-17-slim AS build

# Copy only the POM file to download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project and build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Production Stage
FROM openjdk:17-slim

# Copy built JAR from the build stage
COPY --from=build /target/*.jar app.jar

EXPOSE 8800

ENTRYPOINT ["java", "-jar", "app.jar"]
