# Performance Golf!
[[Install/Run Instructions|Install and Run]] for Live Demonstrations of Java Performance Problems

How quickly can you identify a particular Java performance problem using the least amount of tooling/instrumentation?

The scorecard below determines which approach has the least amount of tooling/instrumentation.  Lowest score wins!  

* 2 stokes if JVM restart is required to hook up your monitoring tool of choice.
* 1 stroke for every separate install process. No strokes for JVM and OS tools. 
* 1 stroke for proprietary tools/techniques, aka specific to a particular vendor.  Ex: Oracle AWR report.  Even ‘EXPLAIN PLAN’ solutions are proprietary.
* 2 strokes: use of high overhead tool, one rarely used in production.  Ex:  java profiler
* 2 stokes for any tool with a $$ licensing cost.
