FROM java:8-jre
VOLUME /tmp
ADD target/mailer-0.0.3-SNAPSHOT.jar app.jar
#Jus to have modification time.
RUN bash -c 'touch /app.jar' 
EXPOSE 8080
ENV LOGGING_LEVEL ="ERROR"
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]