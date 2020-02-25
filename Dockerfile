
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/quotation-back-office-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Xmx200m","-jar","app.jar"]