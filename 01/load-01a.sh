#!/bin/sh
dir=$(dirname "$0")

export JMETER_PLAN=x01a.jmx

$JMETER_HOME/bin/jmeter.sh -n -t $JMETER_PLAN
