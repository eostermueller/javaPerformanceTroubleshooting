#!/bin/sh
dir=$(dirname "$0")

export PG_WAR=$dir/../warProject/target/performanceGolf.war
. $dir/../bin/setenv.sh


$JAVA_HOME/bin/java -Xmx2304m -XX:NewSize=2048m -XX:MaxNewSize=2048m -XX:+UseParallelOldGC -jar $PG_WAR



