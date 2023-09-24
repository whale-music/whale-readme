# build project jar package
FROM openjdk:17-slim-buster
LABEL authors="WhaleMusic"

CMD ["apt", "update"]
CMD ["apt", "install", "curl", "-y"]
CMD ["sh", "/whale/build/build.sh"]