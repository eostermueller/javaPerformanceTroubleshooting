#!/bin/sh
dir=$(dirname "$0")

#export BASE_DIR=$dir/data
export BASE_DIR=$dir/data2
#export BASE_DIR=/Volumes/eto-external-drive/h2

export H2=$dir/../warProject/target/performanceGolf/WEB-INF/lib/h2-1.4.191.jar

java -cp $H2 org.h2.tools.Server -help


