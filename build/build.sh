#!/bin/bash

MIRROR=https://ghproxy.com/
WEB_URL_PATH=${MIRROR}https://github.com/whale-music/whale-music-web/releases/latest/download/dist.tar.gz

ROOT_PATH=$(pwd)/..
WEB_DIR="${ROOT_PATH}/core/src/main/resources/static/web"
# 设置 Java 和 Maven 的环境变量
#export JAVA_HOME=/path/to/your/jdk
export PATH=$JAVA_HOME/bin:$PATH

#export MAVEN_HOME=/path/to/your/maven
export PATH=$MAVEN_HOME/bin:$PATH

# Download web front-end projects
curl -fsSLO $WEB_URL_PATH
# Unzip and delete
tar xzf dist.tar.gz && rm dist.tar.gz
mkdir -p "${WEB_DIR}"
mv dist/* "${WEB_DIR}"
rm -rf dist/

# go into project root path
cd .. || exit

# 构建项目, 检查构建是否成功
if ! ./mvnw -B -s .mvn/settings-mirror.xml clean package -Dmaven.test.skip=true; then
  echo "-------------------------"
  echo "build failed"
fi

# Delete after successful build
rm -rf "${WEB_DIR}"
