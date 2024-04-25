FROM gradle:jdk21-alpine
EXPOSE 8080
RUN mkdir /app
COPY ./build/libs/order-api-1.0.0.jar /app/order-api.jar
ENTRYPOINT ["java","-jar","/app/order-api.jar"]
