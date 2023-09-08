FROM openjdk:17
WORKDIR /app
EXPOSE 8080
COPY /target/script-evaluation-0.0.1-SNAPSHOT.jar script-eval.jar
CMD ["java","-jar","script-eval.jar"]