package org.dartmouth.dao;

import java.util.List;
import java.util.Map;




import org.dartmouth.common.Result;
import org.dartmouth.domain.EventDO;


/**
 * @author Qiuhan Wang
 * @date   Jun 24, 2014
 */

public interface EventDAO {
	
	
	/* Add a new event to the database 
	 * return: event_id if success
	 *         -1 if error
	 */
	Result insertEvent(EventDO event);
	
	/* Delete the event by event id and host id*/
	Result deleteEventByEventIdHostId(Long eventId, Long hostId);
	
	EventDO getEventByEventId(Long id);
	
	/*Query the database based on the parameters and return the result*/
	List<EventDO> query(Map<String, Object> params);
	

	public List<EventDO> getEventByEventZipCode(String zip);
	
	// For testing
	
	List<EventDO> getAll();
	
	int clearAll();
}
