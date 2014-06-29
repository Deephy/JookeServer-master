package org.dartmouth.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dartmouth.cache.LRUCache;
import org.dartmouth.common.Settings;
import org.dartmouth.domain.CacheEventDO;
import org.dartmouth.domain.EventDO;
import org.dartmouth.service.EventCacheService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Yaozhong Kang
 * @date May 23, 2014
 */
@Service
@Transactional
@Qualifier(value = "local")
public class EventCacheLocalCacheImpl extends LRUCache implements
		EventCacheService {

	private static final long serialVersionUID = 73954831791646322L;

	static Logger logger = Logger.getLogger(EventCacheLocalCacheImpl.class
			.getName());
	
	
	private Map<String, List<CacheEventDO>> events;

	/**
	 * use default setting
	 */
	public EventCacheLocalCacheImpl() {
		super(Settings.MAX_EVENT);
		events = new HashMap<String, List<CacheEventDO>>();
	}

	public EventCacheLocalCacheImpl(int capacity) {
		super(capacity);
		events = new HashMap<String, List<CacheEventDO>>();
	}

	@Override
	public void addEvent(CacheEventDO event) {
		this.set(event.getEvent_id(), event);
//		List<CacheEventDO> list = events.get(event.getEvent_zip_code());
//		if (list == null) {
//			list = new LinkedList<CacheEventDO>();
//			events.put(event.getEvent_zip_code(), list);
//		}
//		list.add(event);
	}

	@Override
	public CacheEventDO getEventById(Long eventID) {
		return (CacheEventDO) super.get(eventID);
	}


	@Override
	public CacheEventDO deleteEvent(Long eventID) {
		CacheEventDO e = (CacheEventDO) super.delete(eventID);
		//this.events.get(e.getEvent_zip_code()).remove(e);
		return e;
	}

	@Override
	public void clearMemory() {
		this.events.clear();
		this.map.clear();
		this.list = new DoubleLinkedList();
	}

	@Override
	public LinkedList<CacheEventDO> getAll() {
		LinkedList<CacheEventDO> result = new LinkedList<CacheEventDO>();
		for (Entry<Object, Node> entry : this.map.entrySet()) {
			result.add((CacheEventDO) entry.getValue().getVal());
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
		return null;
	}
}
