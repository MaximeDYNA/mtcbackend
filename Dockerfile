FROM openjdk:8-jdk-alpine
WORKDIR /usr/app
VOLUME /opt
COPY ./target/mtc-* ./
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "mtc-0.0.1.jar"]