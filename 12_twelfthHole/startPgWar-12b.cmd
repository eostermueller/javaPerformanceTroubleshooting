SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

SET PG_WAR=%dir%\..\warProject\target\performanceGolf.war

%JAVA_HOME%\bin\java -Xmx2048m -XX:NewSize=1792m -XX:MaxNewSize=1792m -XX:+UseParallelOldGC -jar %PG_WAR%
