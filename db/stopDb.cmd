SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

%JAVA_HOME%\bin\java -cp %dir%\..\warProject\target\performanceGolf\WEB-INF\lib\h2-1.4.191.jar org.h2.tools.Server -tcpShutdown tcp://localhost:9092
