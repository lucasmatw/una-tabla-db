#Pull the Java base image
FROM maven:3.9.3-amazoncorretto-17

#Set the working directory
WORKDIR /usr/src/mymaven

#Copy the build context
COPY . /usr/src/mymaven

#Compile the Java application
RUN mvn compile assembly:single

CMD java -jar target/una-tabla-db-1.0-SNAPSHOT-jar-with-dependencies.jar "app.db"
