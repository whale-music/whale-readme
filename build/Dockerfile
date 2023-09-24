# build project jar package
FROM openjdk:17-slim-buster
LABEL authors="WhaleMusic"

RUN ["apt", "update"]
RUN ["apt", "install", "curl", "-y"]


CMD ["sh", "/whale/build/build.sh"]