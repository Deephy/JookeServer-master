package org.dartmouth.cache;

import java.util.LinkedList;
import java.util.List;

import org.dartmouth.common.GlobalVariables;
import org.dartmouth.domain.CacheEventDO;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

public class EventCache {
	private static Cache container = null;

	public EventCache() {
		if (container == null) {
			CacheManager manager = CacheManager.create();
			CacheConfiguration cacheConfig = new CacheConfiguration(
					GlobalVariables.CACHE_NAME.EVENT_CACHE, 0);
			manager.addCache(new Cache(cacheConfig));
			this.container = manager
					.getCache(GlobalVariables.CACHE_NAME.EVENT_CACHE);
		}
	}

	public void addEvent(CacheEventDO cEvent) {
		Element e = new Element(cEvent.getEvent_id(), cEvent);
		container.put(e);
	}

	public int deleteEvent(Long eventId) {
		CacheEventDO event = getEventById(eventId);
		// The event is not in the cache
		if (event == null){
			return -1;
		}
		container.remove(eventId);
		return 0;
	}

	public CacheEventDO getEventById(Long eventId) {
		Element e = container.get(eventId);
		// No such kind item in the cache
		if (e == null){
			return null;
		}
		CacheEventDO event = (CacheEventDO)e.getObjectValue();
		return event;
	}

	public String getHostIpById(Long eventId, Long hostId) {
		CacheEventDO event = getEventById(eventId);
		if (event.getHost_id().equals(hostId)){
			return event.getHost_ip();
		}
		return null;
	}
	
	public void clearMemory() {
		container.removeAll();
	}

	@SuppressWarnings("unchecked")
	public LinkedList<CacheEventDO> getAll() {
		LinkedList<CacheEventDO> result = new LinkedList<CacheEventDO>();
		List<Object> keys = (List<Object>) container.getKeys();
		for (Object o : keys) {
			result.add(this.getEventById((Long) o));
		}
		return result;
	}
}
