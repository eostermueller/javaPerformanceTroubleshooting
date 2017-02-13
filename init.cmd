@echo off
set dir=%~dp0

echo "@@"
echo "@@"
echo "@@ About to initialize the performanceGolf war & db."
echo "@@ It will take about 5-10 minutes to populate the db."
echo "@@ To change location of this database, quit this script now and edit db/startDb.cmd."
echo "@@"
echo "@@ Before proceeding:"
echo "@@ 1) Make sure you have 1gb of available disk space."
echo "@@ 2) Make sure Java 1.8 or greater is installed and JAVA_HOME/bin is in the path."
echo "@@ 3) Make sure Maven 3 or greater is installed and MAVEN_HOME/bin is in the path."
echo "@@"
echo "@@ "Ctrl+C to exit, or ...."
pause

set LOG=%dir%\db\h2.log


#Build the WAR file used for all performance testing
mvn -f %dir%\warProject\pom.xml clean package

###
###    Delete any existing database
###
%dir%\db\stopDb.cmd > NUL
rmdir /q /s %dir%\db\data
mkdir %dir%\db\data

###
###    Create blank DB and generate data into it.
###    This is the part that takes 5-10 minutes.
###
start %dir%\db\startDb.cmd
%dir%\load.cmd loadDb-01.jmx
%dir%\load.cmd loadDb-02.jmx
echo "@@"
echo "@@ Here are row counts of tables:"
echo "@@"
%dir%\load.cmd countRows-01.jmx | findstr S0
%dir%\load.cmd countRows-02.jmx | findstr S0
%dir\db\stopDb.cmd

echo "@@"
echo "@@ init.cmd is finished."
echo "@@ The db has been stopped."
echo "@@"