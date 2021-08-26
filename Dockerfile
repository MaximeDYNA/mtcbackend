FROM openjdk:8u111-jdk-alpine
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*
WORKDIR /usr/app
VOLUME /opt
COPY ./target/mtc-* ./
EXPOSE 8086
CMD ["java", "-jar", "-Xmx4096m", "-Dspring.profiles.active=dev", "mtc-0.0.1.jar"]