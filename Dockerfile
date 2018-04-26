FROM java:8
VOLUME /tmp
EXPOSE 8080
ADD /target/order-processing-service-0.0.1-SNAPSHOT.jar order-processing-service-1.0.jar
ENTRYPOINT ["java","-jar","order-processing-service-1.0.jar"]