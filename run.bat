@echo off
setlocal

if not exist MeteorRun.jar (
    call build.bat
    if errorlevel 1 exit /b 1
)

java -jar MeteorRun.jar
endlocal
