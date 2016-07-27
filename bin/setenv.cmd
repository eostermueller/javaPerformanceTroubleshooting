
SET bin_dir=%~dp0
SET bin_dir=%bin_dir:~0,-1%

set H2_JAR=%bin_dir%\..\warProject\target\performanceGolf\WEB-INF\lib\h2-1.4.191.jar
set JMETER_HOME=C:\Users\winSquee\Desktop\jmeter\apache-jmeter-3.0

set JAVA_HOME=C:\Progra~1\Java\jdk1.8.0_91



REM JMeter will use this
set JVM_ARGS=-Xmx3g
