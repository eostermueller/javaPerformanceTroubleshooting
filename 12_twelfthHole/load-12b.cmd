SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

SET JMETER_PLAN=%dir%\x12b.jmx

%JMETER_HOME%\bin\jmeter.bat -n -t %JMETER_PLAN%
