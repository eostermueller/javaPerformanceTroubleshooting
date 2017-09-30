#This scripts takes a single parameter, the 3-character id of the test.
#Each id is a profile in pom-startWar.xml

BASEDIR=$(dirname $0)
mvn -f $BASEDIR/pom-startWar.xml -P$1 clean deploy
