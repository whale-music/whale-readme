#!/bin/bash

MIRROR=https://ghproxy.com/
WEB_URL_PATH=${MIRROR}https://github.com/whale-music/whale-music-web/releases/latest/download/dist.tar.gz

# 获取脚本所在目录的绝对路径
SCRIPT_PATH=$(readlink -f "$0")
# 获取脚本所在目录的绝对路径
CURRENT_DIR=$(dirname "$SCRIPT_PATH")
ROOT_PATH=$(dirname "$CURRENT_DIR")

WEB_DIR="${ROOT_PATH}/core/src/main/resources/static/web"
# 设置 Java 和 Maven 的环境变量
#export JAVA_HOME=/path/to/your/jdk
export PATH=$JAVA_HOME/bin:$PATH

#export MAVEN_HOME=/path/to/your/maven
export PATH=$MAVEN_HOME/bin:$PATH

# Download web front-end projects
if ! curl -fsSLO $WEB_URL_PATH; then
  echo "\033[31mDownload failed\033[0m"
  exit 1
fi
# Unzip and delete
tar xzf dist.tar.gz && rm dist.tar.gz
mkdir -p "${WEB_DIR}"
mv dist/* "${WEB_DIR}"
rm -rf dist/

# 构建项目, 检查构建是否成功
if ! sh "$ROOT_PATH/mvnw" -B -f "$ROOT_PATH/pom.xml" -s "$ROOT_PATH/.mvn/settings-mirror.xml" clean package -Dmaven.test.skip=true; then
  echo "-------------------------"
  echo "\033[31mBuild failed\033[0m"
fi

# Delete after successful build
rm -rf "${WEB_DIR}"
