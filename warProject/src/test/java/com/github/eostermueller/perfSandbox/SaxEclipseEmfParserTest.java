package com.github.eostermueller.perfSandbox;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.eclipse.emf.ecore.xmi.XMLParserPool;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxEclipseEmfParserTest {
	private static final String SIMPLE_XML = "<grandparent><parent><child><age>4</age></child><parent><grandparent>";
	Map<String,Boolean> features = new HashMap<String,Boolean>();
	Map<String,?> properties = new HashMap<String,Object>();

	private XMLParserPool parserPool = null; 
	@Before 
	public void setup() {
		parserPool = new XMLParserPoolImpl();
	}
	@Test
	public void test() throws ParserConfigurationException, SAXException {
		SAXParser parser = parserPool.get(features, properties, false);
	}
}
class MyHandler extends DefaultHandler {
	@Override
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
		System.out.println( "localName [" + localName + "]" );
	}
}
