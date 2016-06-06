#!/bin/sh
dir=$(dirname "$0")

$JAVA_HOME/bin/java -cp $dir/h2/bin/h2*.jar org.h2.tools.Server -tcpShutdown tcp://localhost:9092


#[-baseDir <dir>]        The base directory for H2 databases (all servers)
#[-ifExists]             Only existing databases may be opened (all servers)
