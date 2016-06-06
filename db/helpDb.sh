#!/bin/sh
dir=$(dirname "$0")

#export BASE_DIR=$dir/data
export BASE_DIR=$dir/data2
#export BASE_DIR=/Volumes/eto-external-drive/h2

java -cp $dir/h2/bin/h2*.jar org.h2.tools.Server -help


