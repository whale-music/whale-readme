# build project jar package
FROM openjdk:17-slim-buster
LABEL authors="WhaleMusic"

RUN apt-get update \
    && apt-get install -y curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*


CMD ["sh", "/whale/build/build.sh"]