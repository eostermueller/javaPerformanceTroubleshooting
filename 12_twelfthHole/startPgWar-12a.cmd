SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

SET PG_WAR=%dir%\..\warProject\target\performanceGolf.war

%JAVA_HOME%\bin\java -Xmx512m -XX:NewSize=100m -XX:MaxNewSize=100m -XX:+UseParallelOldGC -jar %PG_WAR%
