FROM openjdk:8-jdk-alpine
WORKDIR /usr/app
COPY ./target/mtc-* ./
EXPOSE 8080
CMD ["java", "-jar", "mtc-0.0.1.jar"]