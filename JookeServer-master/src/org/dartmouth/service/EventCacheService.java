package org.dartmouth.service;

import java.util.LinkedList;
import java.util.List;

import org.dartmouth.domain.CacheEventDO;
import org.dartmouth.domain.EventDO;


public interface EventCacheService {
	
	public void addEvent(CacheEventDO event) ;

	
	
	public CacheEventDO getEventById(Long eventID);

	/* Delete the event by event_id */
	public CacheEventDO deleteEvent(Long eventId);
	

	/* Get all events from the cache */
	public LinkedList<CacheEventDO> getAll();
	
	/* Get the host ip of the event_id and host_id*/
	public String getHostIpById(Long eventId, Long hostId);
	
	/* Load all the events from the database */
	public void loadEvent();
	// For the testing
	public void clearMemory();

	
	
}
