package com.github.eostermueller.perfSandbox;

import java.util.concurrent.atomic.AtomicBoolean;

import com.github.eostermueller.perfSandbox.filesystem.FileSystemReader;

/**
 * Eat up lots of CPU as a troubleshooting exercise
 * @author erikostermueller
 *
 */
public class BusyCpuProcessor {

	public static final long SLEEP_TIME_MS = 50;
	BusyCpuRunner runner = null;
	Thread thread = null;
	String data = null;
	public BusyCpuProcessor() {
		
		FileSystemReader fsr = new FileSystemReader();
		data = fsr.readConfig();
		runner = new BusyCpuRunner( fsr.readConfig() );
		
	}
	public boolean isStarted() {
		boolean rc = false;
		
		if (thread !=null) {
			Thread.State myState = thread.getState();

			if (myState.equals(Thread.State.RUNNABLE))
				rc = true;
		}
		return rc;
	}
	
	public void start() {
		
		stop();
		thread = new Thread(runner, "PerformanceGolfBusyCpuThread");
		thread.start();
	}
	public void stop() {
		runner.stop();
	}
}
class BusyCpuRunner implements Runnable {
	private volatile boolean _stop = false;
	String data = null;
	BusyCpuRunner(String val) {
		this.data = val;
	}
	public void run() {
		_stop = false;
		while(!_stop) {
			data.replaceAll("/^(A*)*$/", "bar");
			//data.replaceAll("foo", "bar");
		}
		
	}
	public void stop() {
		_stop = true;
	}
}