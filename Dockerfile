FROM tomcat:9.0.38
ARG WAR_FILE=*.war
COPY ${WAR_FILE} /usr/local/tomcat/webapps/
EXPOSE 8086
ENTRYPOINT [ "sh", "-c", "java -Dspring.profiles.active=docker -Djava.security.egd=file:/dev/./urandom -jar /usr/local/tomcat/webapps/mtc.war" ]
