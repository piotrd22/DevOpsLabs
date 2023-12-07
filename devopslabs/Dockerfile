FROM gradle:jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/devopslabs
WORKDIR /home/gradle/devopslabs
RUN gradle clean build -x test

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /home/gradle/devopslabs/build/libs/devopslabs-0.0.1-SNAPSHOT.jar /app

ENV DATABASE_URL=${DATABASE_URL:-REPLACE}
ENV DATABASE_USERNAME=${DATABASE_USERNAME:-REPLACE}
ENV DATABASE_PASSWORD=${DATABASE_PASSWORD:-REPLACE}
ENV DEVOPSLABS_PORT=${DEVOPSLABS_PORT:-REPLACE}

ENTRYPOINT ["java","-jar","devopslabs-0.0.1-SNAPSHOT.jar"]