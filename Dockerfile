FROM java:8 as builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

FROM java:8
COPY --from=builder build/libs/*.jar target.jar

ENTRYPOINT ["java","-jar","/target.jar"]


