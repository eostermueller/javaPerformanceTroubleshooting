@echo off
set dir=%~dp0

echo "@@"
echo "@@"
echo "@@ This script will count the number of rows in the database."
echo "@@ It should take less than 30 seconds to run."
echo "@@ Make sure db/startDb.sh is started in a separate window before continuing."
echo "@@"
echo "@@"

echo "Press Ctrl+C to abort, or...."
pause

%dir%\..\load.cmd countRows-01.jmx | findstr S0
%dir%\..\load.cmd countRows-02.jmx | findstr S0

