FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/nuevo-core-0.0.1.jar
COPY ${JAR_FILE} core_backend.jar
EXPOSE 8094
ENTRYPOINT ["java","-jar","app_backend.jar"]