package com.github.eostermueller.perfSandbox;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.github.eostermueller.perfSandbox.parse.ApacheCommonsPooledSaxParserWorker;
import com.github.eostermueller.perfSandbox.parse.EclipseEmfPooledSaxParserWorker;
import com.github.eostermueller.perfSandbox.parse.UnpooledSaxParserWorker;

public class SaxParserTest {

	@Test
	public void testUnpooledImplementation() {
		UnpooledSaxParserWorker worker = new UnpooledSaxParserWorker();
		worker.setup();
		
		for(int  i = 0; i < 5; i++) {
			worker.parse();
			assertEquals("incorrect calculation from data in NCORV.xml data file", new BigDecimal(278.647434).setScale(6, BigDecimal.ROUND_HALF_UP), worker.bdHR7983);
			assertEquals("incorrect calculation from data in NCORV.xml data file", new BigDecimal(229.865371).setScale(6, BigDecimal.ROUND_HALF_UP), worker.bdHR8587);
			assertEquals("incorrect calculation from data in NCORV.xml data file", new BigDecimal(245.139347).setScale(6, BigDecimal.ROUND_HALF_UP), worker.bdHR8995);
		}
	}
	@Test
	public void testApachePooledImplementation() {
		ApacheCommonsPooledSaxParserWorker worker = new ApacheCommonsPooledSaxParserWorker();
		worker.setup();
		
		for(int  i = 0; i < 5; i++) {
			worker.parse();
			assertEquals("incorrect calculation from data in NCORV.xml data file", new BigDecimal(278.647434).setScale(6, BigDecimal.ROUND_HALF_UP), worker.bdHR7983);
			assertEquals("incorrect calculation from data in NCORV.xml data file", new BigDecimal(229.865371).setScale(6, BigDecimal.ROUND_HALF_UP), worker.bdHR8587);
			assertEquals("incorrect calculation from data in NCORV.xml data file", new BigDecimal(245.139347).setScale(6, BigDecimal.ROUND_HALF_UP), worker.bdHR8995);
		}
	}
	@Test
	public void testEclipseEmfPooledImplementation() {
		EclipseEmfPooledSaxParserWorker worker = new EclipseEmfPooledSaxParserWorker();
		worker.setup();
		
		for(int  i = 0; i < 5; i++) {
			worker.parse();
			assertEquals("incorrect calculation from data in NCORV.xml data file", new BigDecimal(278.647434).setScale(6, BigDecimal.ROUND_HALF_UP), worker.bdHR7983);
			assertEquals("incorrect calculation from data in NCORV.xml data file", new BigDecimal(229.865371).setScale(6, BigDecimal.ROUND_HALF_UP), worker.bdHR8587);
			assertEquals("incorrect calculation from data in NCORV.xml data file", new BigDecimal(245.139347).setScale(6, BigDecimal.ROUND_HALF_UP), worker.bdHR8995);
		}
	}
	
}
