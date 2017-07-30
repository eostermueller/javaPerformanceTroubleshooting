call stopWar.cmd
start cmd /c "mvn -f pom-startWar.xml -P%1 clean deploy"
