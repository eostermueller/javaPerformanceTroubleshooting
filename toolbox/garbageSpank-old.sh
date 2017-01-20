dir=$(dirname "$0")


export MY_CP=$dir/../target/heapSpank-0.5.jar:$dir/heapSpank-0.5.jar

export PID_TO_MONITOR=$1
export INTERVAL=1s
export OPTION=-gcold

$JAVA_HOME/bin/jstat $OPTION $PID_TO_MONITOR $INTERVAL | java -classpath $MY_CP com.github.eostermueller.heapspank.garbagespank.console.GarbageSpankConsole -i $INTERVAL
