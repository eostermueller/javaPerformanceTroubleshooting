#!/bin/sh
dir=$(dirname "$0")

export JMETER_PLAN=$1

$JMETER_HOME/bin/jmeter.sh -n -t $JMETER_PLAN
