FROM maven:3.9.4 AS builder
ADD ./pom.xml pom.xml
ADD ./src src/
RUN mvn clean package

FROM openjdk:17
COPY --from=builder target/script-evaluation-0.0.1-SNAPSHOT.jar sciprt-eval.jar
EXPOSE 8081
CMD ["java","-jar","sciprt-eval.jar"]
