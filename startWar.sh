#This scripts takes a single parameter, the 3-character id of the test.
#Each id is a profile in pom-startWar.xml
mvn -f pom-startWar.xml -P$1 clean deploy
