FROM openjdk:11-jdk
CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ARG PROFILE
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

ENTRYPOINT ["java", "-jar", "app.jar"]