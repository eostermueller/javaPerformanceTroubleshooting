#!/bin/sh
dir=$(dirname "$0")

echo "@@"
echo "@@"
echo "@@ About to initialize the performanceGolf db."
echo "@@ It will take about 5-10 minutes to populate the database."
echo "@@ The new db will take 1gb of disk space."
echo "@@ To change location of this database, quit this script now and edit db/startDb.sh."
echo "@@"
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
$dir/db/stopDb.sh
rm -rf $dir/db/data
mkdir $dir/db/data

###
###    Create blank DB and generate data into it.
###
$dir/db/startDb.sh 1> $LOG 2>&1 &
$dir/load.sh loadDb-01.jmx
$dir/load.sh loadDb-02.jmx
echo "@@"
echo "@@ Here are row counts of tables:"
echo "@@"
$dir/load.sh countRows-01.jmx | grep S0
$dir/load.sh countRows-02.jmx | grep S0
$dir/db/stopDb.sh

echo "@@"
echo "@@ init.sh is finished.
echo "@@"

echo "@@ The db has been stopped."
