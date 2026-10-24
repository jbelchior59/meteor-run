@echo off
setlocal

set "ASSET_ZIP=assets\meteor-resources.zip"
set "BUILD_DIR=build"
set "OUTPUT_JAR=MeteorRun.jar"

if not exist "%ASSET_ZIP%" (
    echo [ERRO] Falta %ASSET_ZIP%
    echo Coloca o pacote de recursos na pasta assets e volta a executar build.bat.
    exit /b 1
)

if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"
mkdir "%BUILD_DIR%"

echo [1/3] A compilar Java...
javac -encoding UTF-8 -d "%BUILD_DIR%" files\*.java
if errorlevel 1 exit /b 1

echo [2/3] A extrair recursos...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath '%ASSET_ZIP%' -DestinationPath '%BUILD_DIR%' -Force"
if errorlevel 1 exit /b 1

echo [3/3] A criar %OUTPUT_JAR%...
jar cfe "%OUTPUT_JAR%" files.Main -C "%BUILD_DIR%" .
if errorlevel 1 exit /b 1

echo.
echo Build concluido: %OUTPUT_JAR%
endlocal
