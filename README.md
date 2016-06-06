# perfSandbox
Live Demonstrations of Java Performance Problems

# Install Instructions

## Step 0: Supported Platforms
These Performance examples should run on any platform.  Only MacOs has been tested at this time.

## Step 1: Install Prerequisites
1. Install JDK 1.8 or greater.
2. Maven (http://maven.apache.org).  Make sure 'mvn' is in the PATH.
3. JMeter, 2.13 greater (http://jmeter.apache.org)

## Step 2: Install and Build War File
1. Clone or download the repo from github.com:  https://github.com/eostermueller/perfSandbox
2. Build the war file

  ```bash
  cd warProject
  mvn clean package
  ```
  ...and be sure to look for the "[INFO] BUILD SUCCESS" message and look for the output war file:
  ```
  warProject/target/perfSandbox.war
  ```

## Step 3: Install and Load Database, configure Environment.
1. Download the latest H2 .zip file (http://www.h2database.com/h2-2016-05-26.zip) to the db folder.
2. cd to the db folder and unzip the file.
3. While still in the 'db' folder, run "./loadDb.sh" to populate the H2 database. This script will take a few minutes to run, depending on your hardware.
4. Edit the "bin/setenv.sh".  Make sure the environment variables in the file are set for all command prompts/shells you work with.

## Step 4: Run the performance tests
1. Running tests requires four different command prompts.
