#!/bin/sh
dir=$(dirname "$0")

export BASE_DIR=$dir/data
#export BASE_DIR=$dir/data2
#export BASE_DIR=/Volumes/eto-external-drive/h2/data


#$JAVA_HOME/bin/java -cp $dir/h2/bin/h2*.jar org.h2.tools.Server -ifExists -baseDir $BASE_DIR -tcpAllowOthers
$JAVA_HOME/bin/java -cp $dir/h2/bin/h2*.jar org.h2.tools.Server -baseDir $BASE_DIR


#[-baseDir <dir>]        The base directory for H2 databases (all servers)
#[-ifExists]             Only existing databases may be opened (all servers)
