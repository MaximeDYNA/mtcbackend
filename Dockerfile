FROM tomcat:9.0.38
ARG WAR_FILE=*.war
COPY ${WAR_FILE} /usr/local/tomcat/webapps/
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "managementtools.war"]
CMD ["-start"]
