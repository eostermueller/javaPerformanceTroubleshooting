SET dir=%~dp0
SET dir=%dir:~0,-1%

call %dir%\..\bin\setenv.cmd

SET PG_WAR=%dir%\..\warProject\target\performanceGolf.war

REM %JAVA_HOME%\bin\java -jar %PG_WAR% -Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl
%JAVA_HOME%\bin\java -jar %PG_WAR% 
