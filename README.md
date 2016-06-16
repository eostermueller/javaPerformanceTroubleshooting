# Performance Golf!
```
NEW!  Support for MS-Windows.
Now you can performanceGolf from MS-Win, Mac, or Linux!
```

1. Start by [installing](https://github.com/eostermueller/performanceGolf/wiki/Install-and-Run) the live demonstrations of Java Performance Problems in this repo.

2. Pick one of the six holes of golf to play.
3. One at a time, run the 'a' load test and the 'b' load test for that hole.  Compare the performance of the two.  Which has better response time / throughput?
4. Using the least amount of tooling/instrumentation (see the Scorecard below), identify the performance problem of the slower test.
5. Post your results for that hole of golf to ```codegolf.stackexchange.com```.

## Motivations
This is a crowd-sourced approach to easier and better performance troubleshooting.  Performance problems are everywhere, so java technicians need access to easy-to-use diagnostic tools at every step of the SDLC.


## Scorecard
This scorecard determines the approach with the least amount of tooling/instrumentation.  Lowest score wins!  

* 1 stroke if JVM restart is required to hook up your monitoring tool of choice.
* 1 stroke for use of high overhead tool, one rarely used in production.  Ex:  java profiler
* 1 stroke for any tool with any $$ licensing cost.
* 1 stroke for every separate install process. No strokes for JVM and pre-installed OS tools. 
* 1 stroke for tools/techniques specific to a particular Database vendor.  Ex: Oracle AWR report.  Even ‘EXPLAIN PLAN’ solutions are proprietary.

## Example One -- zero strokes :-D
This example does not use this github repo, but it will give you the general idea.


[This solution](http://www.nurkiewicz.com/2012/08/which-java-thread-consumes-my-cpu.html) to solving a high CPU problem would get lowest=best instrumentatin score:  zero strokes.  Only JVM and OS tools are used (thread dump and top -H).  There are no tool license costs and a JVM restart was not required for the thread dump.

## Example Two -- 4 strokes :-(
This example also does not use this github repo, but it will give you the general idea of what we mean by the best troubleshooting with the least tooling/instrumentation.


A modern, commercial profiler (YourKit, JProfiler, etc...) would easily solve the high CPU problem in example 1.  But look how many strokes (1+1+1+1=4!) are taken off with this approach:
  * 1 stroke because a JVM restart is required to hook up the tool
  * 1 stroke because profilers are generally tools with so much overhead that few operate in a live production environment
  * 1 stroke because there are licensing costs.
  * 1 stroke to install profiling the tool
