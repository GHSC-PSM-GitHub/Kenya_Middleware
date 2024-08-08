# From tomcat:8.0.51-jre8-alpine
FROM tomcat:9-jdk8
RUN rm -rf /usr/local/tomcat/webapps/*
COPY *.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh","run"]
