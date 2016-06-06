#!/bin/sh
dir=$(dirname "$0")

export JMETER_PLAN=x05a.jmx

$JMETER_HOME/bin/jmeter.sh -n -t $JMETER_PLAN
