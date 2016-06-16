SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

SET PG_WAR=%dir%\..\warProject\target\performanceGolf.war


%JAVA_HOME%\bin\java -jar %PG_WAR%
