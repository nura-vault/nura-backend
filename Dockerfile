FROM maven:3.5-jdk-8 AS build
WORKDIR /home
COPY . .
RUN mvn install -DskipTests=true -f pom.xml

FROM gcr.io/distroless/java
WORKDIR /home
COPY --from=build /home/target/nura-1.0.jar /home/nura-1.0.jar
ENTRYPOINT ["java","-jar","nura-1.0.jar"]