FROM tomcat:9.0.38
ARG WAR_FILE=target/*.war
COPY ${WAR_FILE} /usr/local/tomcat/webapps/
WORKDIR /usr/local/tomcat/webapps
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "managementtools-0.0.1-SNAPSHOT.war"]
