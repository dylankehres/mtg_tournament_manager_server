FROM openjdk:8-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY djk-mtg-tournament-manager-firebase-adminsdk-t4pip-bf5f773d1d.json /
ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080