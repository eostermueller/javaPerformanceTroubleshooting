#!/bin/sh
dir=$(dirname "$0")

export JMETER_PLAN=$dir/countRows-01.jmx

$JMETER_HOME/bin/jmeter.sh -Juser.classpath=$dir/h2/bin -n -t $JMETER_PLAN
