FROM gradle:jdk21-alpine AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew build -x test --no-daemon

FROM alpine/java:21-jre
EXPOSE 8080
RUN mkdir /app
COPY --from=build ./home/gradle/src/build/libs/order-api-1.0.0.jar /app/order-api.jar
ENTRYPOINT ["java", "-jar", "/app/order-api.jar"]
