FROM gradle:jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/devopslabs
WORKDIR /home/gradle/devopslabs
RUN gradle build --no-daemon

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /home/gradle/devopslabs/build/libs/devopslabs-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java","-jar","devopslabs-0.0.1-SNAPSHOT.jar"]