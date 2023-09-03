FROM openjdk:17
ADD /target/script-evaluation-0.0.1-SNAPSHOT.jar script-eval.jar
CMD ["java","-jar","script-eval.jar"]