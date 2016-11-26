SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

SET PG_WAR=%dir%\..\warProject\target\performanceGolf.war

%JAVA_HOME%\bin\java -Xmx512m -XX:NewRatio=4 -XX:+UseParallelOldGC -jar %PG_WAR%
