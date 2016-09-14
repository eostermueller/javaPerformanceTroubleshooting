package com.github.eostermueller.perfSandbox;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.eostermueller.perfSandbox.filesystem.FileSystemReader;

public class Test1MbFileSystemRead {

	@Test
	public void test() {
			FileSystemReader fsr = new FileSystemReader();
			String results = fsr.readConfig();
			
			assertTrue("Unable to read 1mb file", results.length() > 1000000 );
	}
}
