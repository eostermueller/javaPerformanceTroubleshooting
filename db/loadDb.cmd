SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

SET JMETER_PLAN_01=%dir%\loadDb-01.jmx
SET JMETER_PLAN_02=%dir%\loadDb-02.jmx


CALL %JMETER_HOME%\bin\jmeter.bat -Juser.classpath=%dir%\..\warProject\target\performanceGolf\WEB-INF\lib\ -n -t %JMETER_PLAN_01%
CALL %JMETER_HOME%\bin\jmeter.bat -Juser.classpath=%dir%\..\warProject\target\performanceGolf\WEB-INF\lib\ -n -t %JMETER_PLAN_02%
