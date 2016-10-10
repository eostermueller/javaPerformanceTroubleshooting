package com.github.eostermueller.perfSandbox.parse;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxParserPool {
	private static GenericObjectPool<SAXParser> pool = null;
    private static class LazyHolder {
        private static final SaxParserPool SINGLETON = new SaxParserPool();
    }

    public static SaxParserPool getInstance() {
        return LazyHolder.SINGLETON;
    }	
    public SAXParser borrowSAXParser() throws Exception {
    	return pool.borrowObject();
    }
    public void returnSAXParser(SAXParser parser) {
    	pool.returnObject(parser);
    }
	public SaxParserPool() {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		
		/**
		 * set attributes on the config object before creating the pool
		 * https://commons.apache.org/proper/commons-pool/apidocs/org/apache/commons/pool2/impl/GenericObjectPoolConfig.html
		 */
		this.pool = new GenericObjectPool<SAXParser>(new Factory(), config);
	}
	
}
class Factory extends BasePooledObjectFactory<SAXParser> {
    static final SAXParserFactory factory = SAXParserFactory.newInstance();

	@Override
	public SAXParser create() throws Exception {
		return factory.newSAXParser();
	}

	@Override
	public PooledObject<SAXParser> wrap(SAXParser mySaxParser) {
		return new DefaultPooledObject<SAXParser>(mySaxParser);
	}
	@Override
	public void passivateObject(PooledObject<SAXParser> pooledObject) {
		//pooledObject.deallocate();
	}

}
