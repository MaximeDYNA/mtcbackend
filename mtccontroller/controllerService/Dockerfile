FROM openjdk:8-jdk-alpine

RUN apk update

# Install tesseract library
RUN apk add --no-cache tesseract-ocr

# Download last language package
RUN mkdir -p /usr/share/tessdata
ADD https://github.com/tesseract-ocr/tessdata/raw/main/ita.traineddata /usr/share/tessdata/ita.traineddata


# Check the installation status
RUN tesseract --list-langs
RUN tesseract -v

WORKDIR /usr/app

COPY ./target/*.jar ./controlService.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=dev", "controlService.jar"]