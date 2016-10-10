package com.github.eostermueller.perfSandbox.parse;

import java.math.BigDecimal;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class NcorvSaxHandler extends DefaultHandler {
		private Ncorv ncorv;
		public NcorvSaxHandler(Ncorv val) {
			this.ncorv  =val;
		}
		CountType current = null;
		@Override
	    public void startElement (String uri, String localName,
                String qName, Attributes attributes) throws SAXException {
			
			if (qName.equals(CountType.HR7983.toString())) {
				this.current = CountType.HR7983;
			} else if (qName.equals(CountType.HR8587.toString())) {
				this.current = CountType.HR8587;
			} else if (qName.equals(CountType.HR8995.toString())) {
				this.current = CountType.HR8995;
			} else {
				this.current = CountType.OTHER;
			}
			//debug("current [" + this.current + "] uri[" + uri + "] localname[" + localName + "] qName[" + qName + "]");
		}	
		public void characters(char ch[], int start, int length) throws SAXException {
			 
			String data = new String(ch, start, length);
			BigDecimal bd = null;
			if (this.current != null) {
				switch(this.current) {
				case HR7983:
					bd = new BigDecimal(data);
					ncorv.setHR7983( ncorv.getHR7983().add(bd) );
					break;
				case HR8587:
					bd = new BigDecimal(data);
					ncorv.setHR8587( ncorv.getHR8587().add(bd) );
					break;
				case HR8995:
					bd = new BigDecimal(data);
					ncorv.setHR8995( ncorv.getHR8995().add(bd) );
					break;
				case OTHER:
					break;
				default:
					break;
				}
			}
			this.current = CountType.OTHER;
		}
	}

