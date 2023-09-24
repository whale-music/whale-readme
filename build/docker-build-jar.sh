#!/bin/bash

# 获取脚本所在目录的绝对路径
SCRIPT_PATH=$(readlink -f "$0")
# 获取脚本所在目录的绝对路径
CURRENT_DIR=$(dirname "$SCRIPT_PATH")
# 项目目录
ROOT_DIR=$(dirname "$CURRENT_DIR")

DOCKER_NAME=whale-music-jar

# 构建docker镜像
if docker images | grep -q $DOCKER_NAME; then
  echo -e "\033[32mImage $DOCKER_NAME existence.\033[0m"
else
  echo -e "\033[33mImage $DOCKER_NAME no existence.\033[0m"
  docker build -t $DOCKER_NAME "$CURRENT_DIR"
fi

# 是否创建容器
DOCKER_CONTAINER_NAME="$DOCKER_NAME-container"
if docker ps -a | grep -q $DOCKER_CONTAINER_NAME; then
  echo -e "\033[32mContainer $DOCKER_NAME existence.\033[0m"
  docker start $DOCKER_CONTAINER_NAME
else
  echo -e "\033[33mContainer $DOCKER_NAME no existence.\033[0m"
  docker run -v "$ROOT_DIR":/whale --name "$DOCKER_CONTAINER_NAME" "$DOCKER_NAME"
fi
