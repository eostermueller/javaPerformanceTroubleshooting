#!/bin/sh
dir=$(dirname "$0")

export PG_WAR=$dir/../warProject/target/performanceGolf.war
. $dir/../bin/setenv.sh


#$JAVA_HOME/bin/java -jar $PG_WAR -Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl
$JAVA_HOME/bin/java -jar $PG_WAR 
