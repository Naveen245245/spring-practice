FROM openjdk:8-jdk-alpine  8u382-al2-native-jdk
WORKDIR /java-sb-test
COPY target/spring-jsp-0.0.1-SNAPSHOT.jar spring-jsp-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/message-server-1.0.0.jar"]