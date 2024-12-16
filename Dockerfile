FROM maven:3.9.8 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package

FROM openjdk:24-slim
WORKDIR /app
ENV ADMIN_PASSWORD=adminPassword
ENV DB_USERNAME=template-admin
ENV DB_PASSWORD=template-admin-password
COPY --from=build /app/target/template-core-0.0.1-SNAPSHOT.jar /app/template-core.jar
ENTRYPOINT ["java", "-jar", "template-core.jar"]