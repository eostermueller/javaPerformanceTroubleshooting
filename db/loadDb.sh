#!/bin/sh
dir=$(dirname "$0")

export JMETER_PLAN_01=$dir/loadDb-01.jmx
export JMETER_PLAN_02=$dir/loadDb-02.jmx


#$JMETER_HOME/bin/jmeter.sh -Juser.classpath=$dir/../warProject/target/performanceGolf/WEB-INF/lib/ -n -t $JMETER_PLAN_01
$JMETER_HOME/bin/jmeter.sh -Juser.classpath=$dir/../warProject/target/performanceGolf/WEB-INF/lib/ -n -t $JMETER_PLAN_02
