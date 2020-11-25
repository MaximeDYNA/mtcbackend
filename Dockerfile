ADD *.war /usr/local/tomcat/webapps/
EXPOSE 8086
CMD ["catalina.sh", "run"]
