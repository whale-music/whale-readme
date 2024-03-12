# build project runtime docker
FROM openjdk:17-slim-buster
LABEL authors="WhaleMusic"

WORKDIR /whale-music

COPY starter/target/whale-music.jar /whale-music/whale.jar

EXPOSE 6780
EXPOSE 6781
EXPOSE 6782
EXPOSE 6783


CMD ["java", "-jar", "./whale.jar"]
