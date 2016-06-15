SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

SET BASE_DIR=%dir%\data

%JAVA_HOME%\bin\java -cp %dir%\h2\bin\%H2_JAR% org.h2.tools.Server -baseDir %BASE_DIR%