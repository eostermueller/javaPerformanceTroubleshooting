SET dir_stop=%~dp0
SET dir_stop=%dir_stop:~0,-1%


java -cp %dir_stop%\..\warProject\target\performanceGolf\WEB-INF\lib\h2-1.4.191.jar org.h2.tools.Server -tcpShutdown tcp://localhost:9092
