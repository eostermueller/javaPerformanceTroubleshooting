package com.github.eostermueller.perfSandbox.cache;

import java.sql.SQLException;

import com.github.eostermueller.perfSandbox.AbstractBranchInquiry;
import com.github.eostermueller.perfSandbox.BranchInquiry;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class CachedBranchInquiryImpl extends AbstractBranchInquiry implements BranchInquiry {
	private CacheManager cm = CacheManager.getInstance();
	//private static CacheManager cm = CacheManager.create();
	private static final String BRANCH_CACHE = "BRANCH_CACHE";
	

	String getKey(long branchId) {
		return Long.toString(branchId).trim();
	}
	@Override
	public String logicalBranchNameInquiry(long branchId) throws SQLException {
		String branchName = null;
		if (!cm.cacheExists(BRANCH_CACHE))
			throw new RuntimeException("Is ehcache in the classpath?  Was expecting a BRACHE_CACHE to be defined in ehcache.xml, like this:  <cache name='BRANCH_CACHE'");
		
		Cache cache = cm.getCache(BRANCH_CACHE);
		
		String key = getKey(branchId);
		if (cache.isKeyInCache(key)) {
			Element e = cache.get(key);
			branchName = (String)e.getObjectValue();
		} else {
			branchName = this.physicalBranchNameInquiry(branchId);
			Element e = new Element(getKey(branchId),branchName);
			cache.put(e);
		}
		
		return branchName;
		
	}
	@Override
	public void shutdown() {
		cm.shutdown();
	}

}
