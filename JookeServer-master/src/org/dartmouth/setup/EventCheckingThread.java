package org.dartmouth.setup;

import java.util.List;

import javax.sql.DataSource;

import org.dartmouth.cache.EventCache;
import org.dartmouth.common.GlobalVariables;
import org.dartmouth.domain.CacheEventDO;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Component
@Scope("prototype")
public class EventCheckingThread extends Thread {
	
	// The global event cache
	public static EventCache g_eventCache = null;

	public void run() {
	   
		g_eventCache = new EventCache();

		while (true) {
			
			// Sleep an interval to avoid busy check
			try {
				Thread.sleep(GlobalVariables.CHECKING_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<CacheEventDO> eventList = g_eventCache.getAll();
            //System.out.println("cache size: " + eventList.size());
            
			// put inside the for loop or outside the for loop?
			Long currentTime = System.currentTimeMillis();
			
			// Go through all the events to check alive
			for (CacheEventDO e : eventList) {
				
			    helper_print_event_cache(e);
			    System.out.println("interval : " + (currentTime - e.getLast_heartbeat_time()) / 1000);
				
				// Event is died
				if (currentTime - e.getLast_heartbeat_time() > GlobalVariables.HEART_BEAT_TOL) {
					System.out.println("event died");
					
					// remove the event from the cache 
					g_eventCache.deleteEvent(e.getEvent_id());

					// delete from the database
					deleteEventFromDB(e.getEvent_id(), e.getHost_id());
	
					continue;
				}

				// event runs beyond time limit
				if (currentTime - e.getEvent_time() > GlobalVariables.EVENT_TIME_LIMIT) {
					// System.out.println("event expired");
					// push notification to the event
				}
			}
			//break;
		}
		
		//return;
	}
	
	
	// delete event from database
	private int deleteEventFromDB(Long eventId, Long hostId){
		
		DataSource ds = getDataSource();
		JdbcTemplate jt = new JdbcTemplate(ds);

		String deleteQuery = "delete from event_table where id = ? and host_id = ?";
		Object[] ps = new Object[2];
		ps[0] = eventId;
		ps[1] = hostId;

		if (jt.update(deleteQuery, ps) != 1) {
			//System.out.println("[ERROR] : DB ERROR");
			return -1;
		}
		
		return 0;
	}

	private DataSource getDataSource() {
		
		String url = "jdbc:mysql://jookeserver.cefr70ovphrc.us-east-1.rds.amazonaws.com:3306/jookedb";
		String driverClassName = "com.mysql.jdbc.Driver";
		String dbUsername = "admin";
		String dbPassword = "admin000";

		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(driverClassName);

		dataSource.setUrl(url);

		dataSource.setUsername(dbUsername);

		dataSource.setPassword(dbPassword);

		return (DataSource)dataSource;

	}
	
	
	public void helper_print_event_cache(CacheEventDO event){
		System.out.println(event.getEvent_id() + "  " + event.getHost_id() + "   " + event.getHost_ip() + "   " + event.getLast_heartbeat_time());
	}

}
