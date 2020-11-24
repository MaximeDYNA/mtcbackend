FROM tomcat:9.0.38
VOLUME /tmp
COPY mtc-0.0.1.war /usr/local/tomcat/webapps/mtc-0.0.1.war
EXPOSE 8086
ENTRYPOINT [ "sh", "-c", "java -Dspring.profiles.active=docker -Djava.security.egd=file:/dev/./urandom -jar /usr/local/tomcat/webapps/mtc-0.0.1.war" ]
