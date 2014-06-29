package org.dartmouth.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.dartmouth.cache.UserCache;
import org.dartmouth.common.CommonUtils;
import org.dartmouth.common.GlobalVariables;
import org.dartmouth.common.Result;
import org.dartmouth.dao.EventDAO;
import org.dartmouth.dao.impl.EventDAOImpl;
import org.dartmouth.domain.CacheEventDO;
import org.dartmouth.domain.EventDO;
import org.dartmouth.service.EventCacheService;
import org.dartmouth.service.EventService;
import org.dartmouth.setup.EventCheckingThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yaozhong Kang
 * @date May 21, 2014
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDAO eventDAO;

	static Logger logger = Logger.getLogger(EventServiceImpl.class.getName());

	@Override
	public Result addEventToDB(EventDO event) {
		Result result = this.eventDAO.insertEvent(event);
		return result;
	}

	@Override
	public void addEventToCache(CacheEventDO event) {
		EventCheckingThread.g_eventCache.addEvent(event);
	}

	@Override
	public PriorityQueue<EventDO> getNearByEvents(final float lat,
			final float lon, String zip) {
		// List<EventDO> events = eventCacheService.getEventsByZip(zip);

		List<EventDO> events = new ArrayList<EventDO>();
		
		// If the zip is not available, get all events
		if (zip == null) {
			events = eventDAO.getAll();
		} else {   // Get events based on the zip code
			events = eventDAO.getEventByEventZipCode(zip);
		}
		
		// No event availlable
		if (events.size() == 0) {
			return new PriorityQueue<EventDO>();
		}
		
		PriorityQueue<EventDO> queue = new PriorityQueue<EventDO>(
				events.size(), new Comparator<EventDO>() {
					@Override
					public int compare(EventDO o1, EventDO o2) {
						float dis1 = CommonUtils.distFrom(o1.getLat(),
								o1.getLon(), lat, lon);
						float dis2 = CommonUtils.distFrom(o2.getLat(),
								o2.getLon(), lat, lon);
						return Float.compare(dis1, dis2);
					}
				});

		for (EventDO e : events) {
			queue.add(e);
		}
		return queue;
	}

	/* Return null if there is no such event in the cache */
	@Override
	public CacheEventDO getEventFromCache(Long id) {
		return EventCheckingThread.g_eventCache.getEventById(id);
	}

	public EventDO getEventFromDB(Long id) {
		EventDO event = eventDAO.getEventByEventId(id);
		return event;
	}

	@Override
	public Result deleteEventFromDB(Long eventId, Long hostId) {
		Result result = this.eventDAO.deleteEventByEventIdHostId(eventId,
				hostId);
		return result;
	}

	public Result deleteEventFromCache(Long eventId, Long hostId) {
		int res = EventCheckingThread.g_eventCache.deleteEvent(eventId);
		Result result = new Result();
		result.setSuccess(res == 0);
		return result;
	}

	// For testing
	@Override
	public List<EventDO> getAll() {
		return eventDAO.getAll();
	}

	@Override
	public void deleteAll() {
		this.eventDAO.clearAll();
	}

}
