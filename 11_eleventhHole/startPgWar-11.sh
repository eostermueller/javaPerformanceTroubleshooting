#!/bin/sh
dir=$(dirname "$0")

export PG_WAR=$dir/../warProject/target/performanceGolf.war
. $dir/../bin/setenv.sh


#$JAVA_HOME/bin/java -XX:NewRatio=8 -jar $PG_WAR
$JAVA_HOME/bin/java -Xmx512m -XX:MaxNewSize=400m -XX:NewSize=400m -jar $PG_WAR
