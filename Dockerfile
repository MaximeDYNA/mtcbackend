FROM openjdk:8u111-jdk-alpine
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*
VOLUME /opt
WORKDIR /usr/app
COPY ./target/mtc-* ./
EXPOSE 8086
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "mtc-0.0.1.jar"]
