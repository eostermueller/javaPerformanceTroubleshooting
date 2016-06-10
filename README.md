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
  <p>
[Step 2 Screencast](http://g.recordit.co/yb8Otyglc2.gif "Animated GIF of install" target="_blank")  

1. Clone or download the repo from github.com:  https://github.com/eostermueller/perfSandbox
2. Build the war file

  <pre>
  cd warProject
  mvn clean package
  </pre>
  ...and be sure to look for the "[INFO] BUILD SUCCESS" message and look for the output war file:
  <pre><code>
  warProject/target/perfSandbox.war
  </code></pre>
  
  

## Step 3: Configure Environment / Install and Load Database.
  <p>
[Step 3 Screencast](http://g.recordit.co/V7vOn5fR1J.gif "Animated GIF of install" target="_blank")  

1. Edit the "bin/setenv.sh".  Make sure the environment variables in the file are set for all command prompts/shells you work with.
2. Download the latest H2 .zip file (http://www.h2database.com/h2-2016-05-26.zip) to the db folder.
3. cd to the db folder and unzip the file.
4. chmd +x *.sh and start the database:  ./startDb.sh
5. While still in the 'db' folder, run "./loadDb.sh" to populate the H2 database. This script will take a few minutes to run, depending on your hardware.  
</pre>


## Step 4: Run the performance tests
[Step 4 Screencast](http://g.recordit.co/GWDvnNfw3A.gif "Animated GIF to run one sandbox test" target="_blank")  


Start by running the "00_warmup" test.  You will need 3 or 4 command prompts to run a sandbox test.  Optionally,  use [tmux](http://www.hamvocke.com/blog/a-quick-and-easy-guide-to-tmux/ "a tmux blog post with install instructions for mac/linux") for mac/linux to create 4 ssh windows in a single osx "Terminal" instance.



| | script | description | notes | to stop the process |
|---|---|---|---|---|
| 1| ./db/startDb.sh | H2 DB | This same process must be started for all sandboxes (00_warmup, 01, 02, etc...) | Use Ctrl+C |
| 2| 00_warmup/startPsWar-warmup.sh | Spring Boot Jetty Web Server | Startup Script specific to the 00_warmup sandbox | Use Ctrl+C |
| 3| 00_warmup/load-00-warmup.sh | JMeter load (via cmd line) | Load script specific to the 00_warmup sandbx | Use Ctrl+C |
| 4| none | none | Used for checking things out, like using JAVA_HoME/bin/jps to confirm which java processes are running ||


Notes about running the tests
1. The 00_warmup folder has just one "load*.sh" script.  But the other numbered folders (01, 02, 05 etc...) have a few "load*.sh" files.  Look for the 'readme-??.txt' files that ask a few questions about the performance problems.
2. What to start/restart and when:
  1. Once you start the H2 db with 'db/startDb.sh', just leave it running until the next time you shut down your machine.
  2. 'cd' to a numbered folder, say 02, and start the .war file with ./startPsWar-02.sh.  Wait for the startup to complete:
  <pre><code>
  2016-06-06 02:34:55.128  INFO 97318 --- [           main] c.g.e.perfSandbox.PerformanceSandboxApp  : Started PerformanceSandboxApp in 9.013 seconds (JVM running for 9.576)
  </code></pre>
  3. Compare performance of the two 'load*.sh' scripts in the 02 folder.  Start in a separate window, cd to the exact same folder as above, 02, and execute the './load-02a.sh' script.  Let it run for 10-20 seconds to get a feel for the performance.  Use Ctrl+C to stop the load script.  Then run   './load-02b.sh'
  <pre><code>
  Eriks-MBP:02 erikostermueller$ ./load-02a.sh 
Creating summariser <summary>
Created the tree successfully using x02a.jmx
Starting the test @ Mon Jun 06 02:37:23 CDT 2016 (1465198643015)
Waiting for possible shutdown message on port 4445
summary +      1 in     1s =    1.1/s Avg:   631 Min:   631 Max:   631 Err:     0 (0.00%) Active: 1 Started: 1 Finished: 0
summary +      5 in   7.1s =    0.7/s Avg:  3322 Min:  2485 Max:  4035 Err:     0 (0.00%) Active: 3 Started: 3 Finished: 0
summary =      6 in     8s =    0.8/s Avg:  2873 Min:   631 Max:  4035 Err:     0 (0.00%)
summary +      8 in     9s =    0.9/s Avg:  3396 Min:  3236 Max:  3504 Err:     0 (0.00%) Active: 3 Started: 3 Finished: 0
</code></pre>
