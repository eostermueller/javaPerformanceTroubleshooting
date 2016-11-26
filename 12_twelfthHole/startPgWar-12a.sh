#!/bin/sh
dir=$(dirname "$0")

export PG_WAR=$dir/../warProject/target/performanceGolf.war
. $dir/../bin/setenv.sh

$JAVA_HOME/bin/java -Xmx512m -XX:NewSize=100m -XX:MaxNewSize=100m -XX:+UseParallelOldGC -jar $PG_WAR

