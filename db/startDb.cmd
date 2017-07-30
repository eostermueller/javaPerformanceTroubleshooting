SET dir_db=%~dp0
SET dir_db=%dir_db:~0,-1%


SET BASE_DIR=%dir_db%\data

java -cp %dir_db%\..\warProject\target\performanceGolf\WEB-INF\lib\h2-1.4.191.jar org.h2.tools.Server -baseDir %BASE_DIR%
