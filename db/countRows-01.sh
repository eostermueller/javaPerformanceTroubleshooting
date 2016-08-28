#!/bin/sh
dir=$(dirname "$0")

export JMETER_PLAN=$dir/countRows-01.jmx

$JMETER_HOME/bin/jmeter.sh -Juser.classpath=$dir/../warProject/target/performanceGolf/WEB-INF/lib/ -n -t $JMETER_PLAN
