SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd
java -cp %dir%\%H2_JAR% org.h2.tools.Server -help
