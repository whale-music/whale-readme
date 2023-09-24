# build project runtime docker
FROM openjdk:17-slim-buster
LABEL authors="WhaleMusic"

WORKDIR /

COPY starter/target/whale-music*.jar /whale.jar

EXPOSE 6780
EXPOSE 6781
EXPOSE 6782
EXPOSE 6783


CMD ["java", "-jar", "/whale.jar"]
