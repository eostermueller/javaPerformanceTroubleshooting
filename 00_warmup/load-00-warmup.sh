#!/bin/sh
dir=$(dirname "$0")
. $dir/../bin/setenv.sh
export JMETER_PLAN=$dir/x00_warmup.jmx

$JMETER_HOME/bin/jmeter.sh -n -t $JMETER_PLAN
