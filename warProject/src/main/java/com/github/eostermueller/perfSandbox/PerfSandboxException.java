package com.github.eostermueller.perfSandbox;

public class PerfSandboxException extends Exception {
	Exception cause = null;
	public PerfSandboxException(String string) {
		super(string);
	}
	public PerfSandboxException(String string, Exception e) {
		super(string);
		this.cause = e;
	}
	@Override
	public Exception getCause() {
		return this.cause;
	}
	
	public PerfSandboxException(Exception e) {
		this.cause = e;
	}

}
