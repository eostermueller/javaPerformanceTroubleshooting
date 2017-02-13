#!/bin/sh
dir=$(dirname "$0")

echo "@@"
echo "@@"
echo "@@ This script will count the number of rows in the database."
echo "@@ It should take less than 30 seconds to run."
echo "@@ Make sure db/startDb.sh is started in a separate window before continuing."
echo "@@"
echo "@@"

read -p "Press any key to continue, or press Ctrl+C to abort."

$dir/../load.sh countRows-01.jmx | grep S0
$dir/../load.sh countRows-02.jmx | grep S0

