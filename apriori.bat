@echo off
for /f "tokens=3" %%v in ('java -version 2^>^&1 ^| findstr /i "version"') do set JAVA_VERSION=%%v
set JAVA_VERSION=%JAVA_VERSION:"=%
set MAJOR_VERSION=%JAVA_VERSION:~0,2%

REM Enable native access only if Java 17+
set NATIVE_ACCESS=

if %MAJOR_VERSION% GEQ 17 (
    set NATIVE_ACCESS=--enable-native-access=ALL-UNNAMED
)


java -Dorg.jline.terminal.exec.redirectPipeCreationMode=native %NATIVE_ACCESS% -jar apriori-1.0-SNAPSHOT.jar 
pause