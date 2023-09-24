#!/bin/bash

DOCKER_NAME=whale-music-jar

if docker ps -q | grep -q $(docker ps -aqf "name=$DOCKER_NAME"); then
  echo -e "\033[32mContainer $DOCKER_NAME existence.\033[0m"
else
  echo -e "\033[33mContainer $DOCKER_NAME no existence.\033[0m"
  docker build -t $DOCKER_NAME .
fi

docker -v ../../whale:/whale run -d $DOCKER_NAME --name $DOCKER_NAME