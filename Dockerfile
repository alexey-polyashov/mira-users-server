FROM java:8-jdk
WORKDIR /usr/local/app
ADD /target/mira-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD java -jar ./app.jar --spring.profiles.active=dev