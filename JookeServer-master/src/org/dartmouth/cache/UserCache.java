package org.dartmouth.cache;

import java.util.LinkedList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.config.Searchable;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import net.sf.ehcache.Element;

import org.dartmouth.common.GlobalVariables;
import org.dartmouth.domain.CacheEventDO;
import org.dartmouth.domain.CachePasswordDO;
import org.dartmouth.domain.EventDO;


public class UserCache {

	private static Cache container = null;

	public UserCache() {
		if (container == null) {
			
			CacheManager manager = CacheManager.create();
			CacheConfiguration cacheConfig = new CacheConfiguration(
					GlobalVariables.CACHE_NAME.USER_CACHE, 0);
			
			Searchable searchable = new Searchable();
			cacheConfig.addSearchable(searchable);
			
			searchable.addSearchAttribute(new SearchAttribute().name(
					"caseKey").expression("value.getKey()"));
			
			manager.addCache(new Cache(cacheConfig));
			this.container = manager
					.getCache(GlobalVariables.CACHE_NAME.USER_CACHE);
		}
	}

	public int addCase(CachePasswordDO passwordCase) {
		Element e = new Element(passwordCase.getUserId(), passwordCase);
        container.put(e);
		return 0;
	}

	public int deleteCase(Long userId) {
        container.remove(userId);
        // check the return value of the remove operation
		return 0;
	}

	public CachePasswordDO findCaseById(Long userId) {
        Element e = container.get(userId);
        if (e == null) return null;
        
        CachePasswordDO pCase = (CachePasswordDO) e.getObjectValue();
		return pCase;
	}
	
	public List<CachePasswordDO>getCaseByKey(String caseKey){
		
		Attribute<String> case_key = container
				.getSearchAttribute("caseKey");
		
		Results results = container.createQuery().addCriteria(case_key.eq(caseKey))
				.includeValues().execute();

		List<Result> list = results.all();
		List<CachePasswordDO> caseList = new LinkedList<CachePasswordDO>();
		for (Result r : list) {
			caseList.add((CachePasswordDO) r.getValue());
		}
		return caseList;
	}

}
