@REM Maven Wrapper script for Windows
@REM Simplified version that handles JAVA_HOME paths with spaces

@echo off
setlocal

set "MAVEN_PROJECTBASEDIR=%~dp0"
set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"

if not "%JAVA_HOME%"=="" (
    set "JAVACMD=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVACMD=java.exe"
)

"%JAVACMD%" ^
    -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" ^
    %MAVEN_OPTS% ^
    -classpath "%WRAPPER_JAR%" ^
    org.apache.maven.wrapper.MavenWrapperMain ^
    %*

endlocal
