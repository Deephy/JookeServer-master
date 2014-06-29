package org.dartmouth.service.impl;

import java.util.LinkedList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.config.Searchable;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

import org.apache.log4j.Logger;
import org.dartmouth.domain.CacheEventDO;
import org.dartmouth.domain.EventDO;
import org.dartmouth.service.EventCacheService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Qualifier(value = "ehcache")
public class EventCacheEhCacheImpl implements EventCacheService {

	static Logger logger = Logger.getLogger(EventCacheEhCacheImpl.class
			.getName());

	private Cache container = null;

	public EventCacheEhCacheImpl() {
		Configuration cacheManagerConfig = new Configuration();
		CacheConfiguration cacheConfig = new CacheConfiguration("eventCache", 0);
		
//		Searchable searchable = new Searchable();
//		cacheConfig.addSearchable(searchable);
//		searchable.addSearchAttribute(new SearchAttribute().name(
//				"event_zip_code").expression("value.getEvent_zip_code()"));

		CacheManager manager = CacheManager.newInstance(cacheManagerConfig);
		manager.addCache(new Cache(cacheConfig));
		container = manager.getCache("eventCache");
	}

	@Override
	public void addEvent(CacheEventDO event) {
		Element e = new Element(event.getEvent_id(), event);
		container.put(e);
	}

	@Override
	public CacheEventDO getEventById(Long eventID) {
		Element e = container.get(eventID);
		// No such kind item in the cache
		if (e == null){
			return null;
		}
		CacheEventDO event = (CacheEventDO)e.getObjectValue();
		return event;
	}

	@Override
	public CacheEventDO deleteEvent(Long eventID) {
		CacheEventDO result = (CacheEventDO) container.get(eventID).getObjectValue();
		container.remove(eventID);
		return result;
	}

	@Override
	public void clearMemory() {
		container.removeAll();
	}

	@Override
	@SuppressWarnings("unchecked")
	public LinkedList<CacheEventDO> getAll() {
		LinkedList<CacheEventDO> result = new LinkedList<CacheEventDO>();
		List<Object> keys = (List<Object>) container.getKeys();
		for (Object o : keys) {
			result.add(this.getEventById((Long) o));
		}
		return result;
	}

	@Override
	public void loadEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHostIpById(Long eventId, Long hostId) {
		// TODO Auto-generated method stub
		CacheEventDO event = getEventById(eventId);
		if (event.getHost_id().equals(hostId)){
			return event.getHost_ip();
		}
		return null;
	}
}
