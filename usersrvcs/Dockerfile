FROM openjdk:8-jdk-alpine
# Add evironment variable
ARG APP_JAR_FILE=target/*.jar
# Ceate sbuser for sping boot user in the container
# Change to and become that sbuser
# Set the work directory as home of sbuser
RUN mkdir -p /home/sbuser/app
WORKDIR /home/sbuser
RUN date +ON-%Y-%m-%d-AT-%d-%H:%M:%S > ./image-build-timestamp.txt
# Copy the newly build jar into Jars folder in the sbuser
COPY ${APP_JAR_FILE} ./app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","./app/app.jar"]
