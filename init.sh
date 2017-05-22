#!/bin/sh
dir=$(dirname "$0")

echo "@@"
echo "@@"
echo "@@ About to initialize the performanceGolf war & db."
echo "@@ It will take about 5-10 minutes to populate the db."
echo "@@ To change location of this database, quit this script now and edit db/startDb.sh."
echo "@@"
echo "@@ Before proceeding:"
echo "@@ 1) Make sure you have 1gb of available disk space."
echo "@@ 2) Make sure Java 1.8 or greater is installed and JAVA_HOME/bin is in the path."
echo "@@ 3) Make sure Maven 3 or greater is installed and MAVEN_HOME/bin is in the path."
echo "@@"
read -p "@@ Press any key to continue, or Ctrl+C to exit."

export LOG=$dir/db/h2.log

chmod +x load.sh
chmod +x startWar.sh
chmod +x db/startDb.sh
chmod +x db/stopDb.sh
chmod +x db/countRows.sh
chmod +x db/helpDb.sh

#Build the WAR file used for all performance testing
mvn -f warProject/pom.xml clean package

###
###    Delete any existing database
###
$dir/db/stopDb.sh 1> /dev/null 2>&1
rm -rf $dir/db/data
mkdir $dir/db/data

###
###    Create blank DB and generate data into it.
###    This is the part that takes 5-10 minutes.
###
$dir/db/startDb.sh 1> $LOG 2>&1 &
$dir/load.sh loadDb-01
$dir/load.sh loadDb-02
echo "@@"
echo "@@ Here are row counts of tables:"
echo "@@"
$dir/load.sh countRows-01.jmx | grep S0
$dir/load.sh countRows-02.jmx | grep S0
$dir/db/stopDb.sh

echo "@@"
echo "@@ init.sh is finished."
echo "@@ The db has been stopped."
echo "@@"
