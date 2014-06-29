package org.dartmouth.service;

import java.util.List;
import java.util.PriorityQueue;

import org.dartmouth.common.Result;
import org.dartmouth.domain.CacheEventDO;
import org.dartmouth.domain.EventDO;

/**
 * @author Yaozhong Kang
 * @date May 21, 2014
 */
public interface EventService {
	
	/* Add event to the database*/
	public Result addEventToDB(EventDO event);
	
	/* Add ligth event to the Cache*/
	public void addEventToCache(CacheEventDO event);

	public Result deleteEventFromDB(Long eventId, Long hostId);
	
	public Result deleteEventFromCache(Long eventId, Long hostId);
	
	public EventDO getEventFromDB(Long id);
	
	public CacheEventDO getEventFromCache(Long id);

	public PriorityQueue<EventDO> getNearByEvents(float lat, float lon, String zip);
	
	/**** just for testing, it is dangerous in production environment ****/
	public List<EventDO> getAll();

	public void deleteAll();

	

}
