SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd
SET JMETER_PLAN=%dir%\x00_warmup.jmx

%JMETER_HOME%\bin\jmeter.bat -n -t %JMETER_PLAN%
