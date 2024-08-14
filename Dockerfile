FROM maven:3-openjdk-17 AS build
COPY . /home/maven/src
WORKDIR /home/maven/src
RUN mvn package

FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/maven/src/target/*-with-dependencies.jar /app/ktor-microservice-template.jar
ENTRYPOINT ["java","-jar","/app/ktor-microservice-template.jar"]