FROM openjdk:21
COPY ./build/libs/member-service.jar member-service.jar
#ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "member-service.jar"]
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "member-service.jar"]