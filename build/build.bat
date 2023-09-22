:: @echo off
setlocal
chcp 65001

set MIRROR=https://ghproxy.com/
set WEB_URL_PATH=%MIRROR%https://github.com/whale-music/whale-music-web/releases/latest/download/dist.zip

set ROOT_PATH=%cd%\..
set WEB_DIR="%ROOT_PATH%\core\src\main\resources\static\web"

:: 设置 Java 和 Maven 的环境变量
:: set JAVA_HOME=C:\path\to\your\jdk
set PATH=%JAVA_HOME%\bin;%PATH%
:: set MAVEN_HOME=C:\path\to\your\maven
set PATH=%MAVEN_HOME%\bin;%PATH%

:: 下载 web 前端项目
powershell -command "Invoke-WebRequest -Uri %WEB_URL_PATH% -OutFile dist.zip"
:: 解压并删除
powershell -command "Expand-Archive -Path 'dist.zip' -DestinationPath '.'"
del "dist.zip"
mkdir %WEB_DIR%
xcopy "dist\*" %WEB_DIR% /E /Y
rmdir /s /q dist\

:: 进入项目根路径
cd ..

:: 构建项目，检查构建是否成功
mvnw.cmd -s .mvn\settings-mirror.xml clean package -Dmaven.test.skip=true
if %errorlevel% != 0 (
  echo -------------------------
  echo Build failed
)

:: 构建成功后删除 web 目录
rmdir /s /q %WEB_DIR%

endlocal