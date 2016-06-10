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
5. While still in the 'db' folder, run "./loadDb.sh" to populate the H2 database. This script will take a few minutes to run, depending on your hardware.  The output will end with this:
   <code>
summary = 1881122 in    71s = 26530.9/s Avg:     0 Min:     0 Max:   203 Err:     0 (0.00%)
summary + 811585 in    30s = 27053.7/s Avg:     0 Min:     0 Max:    81 Err:     0 (0.00%) Active: 20 Started: 20 Finished: 0
summary = 2692707 in   101s = 26686.4/s Avg:     0 Min:     0 Max:   203 Err:     0 (0.00%)

BRANCHES=100
ACCOUNTS=250000
HISTORY=2500000
#####END OF DB LOAD #####
</code>


## Step 4: Run the performance tests
[Step 4 Screencast](http://g.recordit.co/GWDvnNfw3A.gif "Animated GIF to run one sandbox test" target="_blank")  


Start by running the "00_warmup" test.  You will need 3 or 4 command prompts to run a sandbox test.  optionally, I use [tmux](http://www.hamvocke.com/blog/a-quick-and-easy-guide-to-tmux/ "a tmux blog post with install instructions for mac/linux") to create 4 ssh windows in a single osx "Terminal" instance.



| | script | description | notes | to stop the process |
|---|---|---|---|---|
| 1| ./db/startDb.sh | H2 DB | This same process must be started for all sandboxes (00_warmup, 01, 02, etc...) | Use Ctrl+C |
| 2| 00_warmup/startPsWar-warmup.sh | Spring Boot Jetty Web Server | Startup Script specific to the 00_warmup sandbox | Use Ctrl+C |
| 3| 00_warmup/load-00-warmup.sh | JMeter load (via cmd line) | Load script specific to the 00_warmup sandbx | Use Ctrl+C |
| 4| none | none | Used for checking things out, like using JAVA_HoME/bin/jps to confirm which java processes are running ||

Running tests requires three different command prompts.  For *nix systems, I suggest using tmux (https://tmux.github.io/) to manage these windows.  You will need one window for each of the following.
1. Running H2 database ('db/startDb.sh').
2. Running war file (look for startPsWar-??.sh scripts)
3. Applying load (with JMeter) to stress the war.
...and I recommend opening a fourth window for troubleshooting.

Notes about running the tests
1. Each numbered folder (00_warmup, 01, 02, 03 etc...) contains a few tests.  Look for the 'readme-??.txt' files that ask a few questions about the performance problems.
2. What to start/restart and when:
  1. once you start the H2 db with 'db/startDb.sh', just leave it running until the next time you shut down your machine.
  2. 'cd' to a numbered folder, say 02, and start the .war file with ./startPsWar-02.sh.  Wait for the startup to complete:
  <pre><code>
  2016-06-06 02:34:55.128  INFO 97318 --- [           main] c.g.e.perfSandbox.PerformanceSandboxApp  : Started PerformanceSandboxApp in 9.013 seconds (JVM running for 9.576)
  </code></pre>
  3. In a separate window, cd to the exact same folder as above, 02, and execute the './load-02a.sh' script.
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
