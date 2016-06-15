SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

%JAVA_HOME%\bin\java -cp %dir%\h2\bin\%H2_JAR% org.h2.tools.Server -tcpShutdown tcp://localhost:9092