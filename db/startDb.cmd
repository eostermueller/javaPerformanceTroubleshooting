SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

SET BASE_DIR=%dir%\data

%JAVA_HOME%\bin\java -cp %dir%\..\warProject\target\performanceGolf\WEB-INF\lib\h2-1.4.191.jar org.h2.tools.Server -baseDir %BASE_DIR%
