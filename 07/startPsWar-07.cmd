SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

SET PS_WAR=%dir%\..\warProject\target\perfSandbox.war

%JAVA_HOME%\bin\java -jar %PS_WAR%
