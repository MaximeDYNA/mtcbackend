FROM openjdk:8u111-jdk-alpine
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*
VOLUME /opt
WORKDIR /usr/app
COPY ./target/mtc-* ./
EXPOSE 8086
<<<<<<< HEAD
CMD ["java", "-jar", "-Xmx4096m", "-Dspring.profiles.active=dev", "mtc-0.0.1.jar"]


#FROM tomcat:9.0.38
#VOLUME /tmp
#WORKDIR /usr/app
#COPY ./target/mtc-* ./
#"EXPOSE 8086
#CMD ["java", "-jar", "-Dspring.profiles.active=dev", "mtc-0.0.1.jar"]



#FROM openjdk:8-jdk-alpine
#WORKDIR /usr/app
#COPY ./target/mtc-* ./
#EXPOSE 8086
#CMD ["ja

=======
#CMD ["java", "-jar", "-Xmx4096m", "-Dspring.profiles.active=dev", "mtc-0.0.1.jar"]
CMD ["java", "-jar",  "-Dspring.profiles.active=dev", "mtc-0.0.1.jar"]
>>>>>>> 11b449e52c495a0d857aa9ee434ce31b2e5c6bca
