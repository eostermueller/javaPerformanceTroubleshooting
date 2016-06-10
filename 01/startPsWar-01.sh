#!/bin/sh
dir=$(dirname "$0")

export PS_WAR=$dir/../warProject/target/perfSandbox.war
. $dir/../bin/setenv.sh


$JAVA_HOME/bin/java -jar $PS_WAR
