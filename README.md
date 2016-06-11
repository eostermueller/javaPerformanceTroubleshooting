# Performance Golf!
[Install/Run Instructions](https://github.com/eostermueller/perfSandbox/wiki/Install-and-Run) for Live Demonstrations of Java Performance Problems

How quickly can you identify a particular Java performance problem using the least amount of tooling/instrumentation?

This scorecard determines the approach with the least amount of tooling/instrumentation.  Lowest score wins!  

* 2 strokes if JVM restart is required to hook up your monitoring tool of choice.
* 2 strokes: use of high overhead tool, one rarely used in production.  Ex:  java profiler
* 2 stokes for any tool with any $$ licensing cost.
* 1 stroke for every separate install process. No strokes for JVM and pre-installed OS tools. 
* 1 stroke for tools/techniques specific to a particular Database vendor.  Ex: Oracle AWR report.  Even ‘EXPLAIN PLAN’ solutions are proprietary.

## Example One -- zero strokes :-D

[This solution](http://www.nurkiewicz.com/2012/08/which-java-thread-consumes-my-cpu.html) to solving a high CPU problem would get lowest=best instrumentatin score:  zero strokes.  Only JVM and OS tools are used (thread dump and top -H).  There are no tool license costs and a JVM restart was not required for the thread dump.

## Example Two -- 7 strokes :-(
A modern, commercial profiler (YourKit, JProfiler, etc...) would easily solve the high CPU problem in example 1.  But look how many strokes (2+2+2+1=7!) are taken off with this approach:
  * 2 strokes because a JVM restart is required to hook up the tool
  * 2 strokes because profilers are generally tools with so much overhead that few operate in a live production environment
  * 2 strokes because there are licensing costs.
  * 1 stroke to install profiling the tool
