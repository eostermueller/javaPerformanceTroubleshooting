package com.github.eostermueller.perfSandbox.parse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;



public class UnpooledSaxParserWorker implements Ncorv {
	private static final String XML_INPUT_FILE = "/NCORV.xml";
	private String xml;
	public BigDecimal bdHR7983;
	public BigDecimal bdHR8587;
	public BigDecimal bdHR8995;
	
	public void setup() {
		InputStream xmlStream = UnpooledSaxParserWorker.class.getResourceAsStream(XML_INPUT_FILE);
		this.xml = new Scanner(xmlStream,"UTF-8").useDelimiter("\\A").next();
	}
	public void parse() {

		this.bdHR7983 = new BigDecimal(0);
		this.bdHR8587  = new BigDecimal(0);
		this.bdHR8995 = new BigDecimal(0);
		
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {

            SAXParser      saxParser = factory.newSAXParser();
            NcorvSaxHandler handler   = new NcorvSaxHandler(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
            saxParser.parse( bais, handler);

        } catch (Throwable err) {
            err.printStackTrace ();
        }		
	}

	@Override
	public BigDecimal getHR7983() {
		return bdHR7983;
	}
	@Override
	public BigDecimal getHR8587() {
		return bdHR8587;
	}
	@Override
	public BigDecimal getHR8995() {
		return bdHR8995;
	}
	@Override
	public void setHR7983(BigDecimal val) {
		bdHR7983  = val;
		
	}
	@Override
	public void setHR8587(BigDecimal val) {
		bdHR8587 = val;
		
	}
	@Override
	public void setHR8995(BigDecimal val) {
		this.bdHR8995 = val;
	}

}
