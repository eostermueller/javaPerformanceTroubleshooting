#!/bin/sh
dir=$(dirname "$0")

export PG_WAR=$dir/../warProject/target/performanceGolf.war
. $dir/../bin/setenv.sh

$JAVA_HOME/bin/java -jar $PG_WAR
