package com.github.eostermueller.perfSandbox.filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Scanner;

public class FileSystemReader {

	private static final String DATA_FILE = "/data_1mb.txt";
	
	public String readConfig() {
		return readFile(DATA_FILE);
	}
	
	private String readFile(String fileName) {
		InputStream in = this.getClass().getResourceAsStream(fileName);
		String text = new Scanner( in ).useDelimiter("\\A").next();
		return text;
	}
}
