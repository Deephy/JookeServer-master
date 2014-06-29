package org.dartmouth.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.dartmouth.common.GlobalVariables;
import org.dartmouth.common.Result;
import org.dartmouth.dao.EventDAO;
import org.dartmouth.domain.EventDO;
import org.dartmouth.domain.ParticipantDO;
import org.dartmouth.setup.EventCheckingThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class EventDAOImpl implements EventDAO {

	@Autowired
	private JdbcTemplate jdbcTemplateEvent;

	
//	public EventDAOImpl(DataSource ds){
//		if (jdbcTemplateEvent == null){
//			jdbcTemplateEvent = new JdbcTemplate(ds);
//		}
//	}
	
	
	@Override
	public Result insertEvent(EventDO event) {

		Result result = new Result();
		
		// Check the existence of event by host_id
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("host_id", event.getHost().getId());
		List<EventDO> eventList = query(map);

		// The host has already host an event
		// need to check whether the user existed in the database?
		if (eventList.size() > 0) {
			System.out.println("duplicate");
			result.setSuccess(false);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.EVENT_EXIST);
			result.setResultObj(-1L);
			return result;
		}

		// Insert the event to the database
		String inserQuery = "insert into event_table "
				+ "(event_name,event_mode,event_zip_code,event_time,allow_add,latitude,longitude,host_id, host_ip, host_profile_img, host_name) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)  ";

		Object[] params = new Object[] { event.getEvent_name(),
				event.getEvent_mode(), event.getEvent_zip_code(),
				event.getEvent_time(), event.getAllow_add(), event.getLat(),
				event.getLon(), event.getHost().getId(),
				event.getHost().getHost_ip(), event.getHost().getProfile_img(),
				event.getHost().getName() };

		int[] types = new int[] { Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR,
				Types.BIGINT, Types.BOOLEAN, Types.FLOAT, Types.FLOAT,
				Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };

		if (jdbcTemplateEvent.update(inserQuery, params, types) != 1) {
			result.setSuccess(false);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.DB_ERROR);
			result.setResultObj(-1L);
			return result;
		}

		eventList = query(map);
		if (eventList.size() == 0) {
			result.setSuccess(false);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.DB_ERROR);
			result.setResultObj(-1L);
			return result;
		}

		//return eventList.get(0).getEvent_id();
		result.setSuccess(true);
		result.setMsg("insert success");
		result.setResultObj(eventList.get(0).getEvent_id());
		return result;
	}

	@Override
	public Result deleteEventByEventIdHostId(Long eventId, Long hostId) {
		// TODO Auto-generated method stub
		Result result = new Result();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_ID, eventId);
		map.put(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_HOST_ID, hostId);
		List<EventDO> eventList = query(map);

		// No such event in the database, query by the host_id matches event_id
		if (eventList.size() == 0){
			result.setSuccess(false);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.EVENT_USER_MISMATCH);
			result.setResultObj(-1L);
			return result;
		}
	
		// Delete the event
		String deleteQuery = "delete from event_table where id = ? and host_id = ?";
		Object[] ps = new Object[2];
		ps[0] = eventId;
		ps[1] = hostId;
       
		if (jdbcTemplateEvent.update(deleteQuery, ps) != 1){
			result.setSuccess(false);
			result.setMsg(GlobalVariables.RESPONSE_MESSAGES.DB_ERROR);
			result.setResultObj(-1L);
			return result;
		}
		
		result.setSuccess(true);
		result.setMsg(null);
		result.setResultObj(eventId);
		
		return result;
	}

	@Override
	public EventDO getEventByEventId(Long id) {
		// TODO Auto-generated method stub

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		List<EventDO> eventList = query(map);

		if (eventList.size() == 0)
			return null;

		return eventList.get(0);
	}
	
	
	/* 
	 * Get events hold at zip code area
	 * Need to have more advanced query to search the events around a zip code area
	 */
	
	@Override
	public List<EventDO> getEventByEventZipCode(String zip){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_EVENT_ZIP_CODE, zip);
		List<EventDO> eventList = query(map);
		return eventList;
	}

	@Override
	public List<EventDO> query(Map<String, Object> params) {
		// TODO Auto-generated method stub

		StringBuffer sql = new StringBuffer(
				"select * from event_table where 1=1");
		Object[] ps = new Object[params.size()];
		int index = 0;
		for (Entry<String, Object> entry : params.entrySet()) {
			sql.append(" and ");
			sql.append(entry.getKey() + "=?");
			ps[index++] = entry.getValue();
		}
		sql.append(" order by id desc");
		System.out.println("sql query: " + sql.toString());
		List<EventDO> result = jdbcTemplateEvent.query(sql.toString(), ps,
				new ResultSetExtractor<List<EventDO>>() {
					@Override
					public List<EventDO> extractData(ResultSet rs)
							throws SQLException, DataAccessException {

						List<EventDO> list = new ArrayList<EventDO>();
						while (rs.next()) {

							// get whole column information from database
							EventDO e = new EventDO();
							ParticipantDO host = new ParticipantDO();
							e.setEvent_id(rs
									.getLong(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_ID));
							e.setEvent_mode(rs
									.getBoolean(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_EVENT_MODE));
							e.setEvent_name(rs
									.getString(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_EVENT_NAME));
							e.setEvent_time(rs
									.getLong(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_EVENT_TIME));
							e.setEvent_zip_code(rs
									.getString(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_EVENT_ZIP_CODE));
							e.setAllow_add(rs
									.getBoolean(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_ALLOW_ADD));
							e.setLat(rs
									.getFloat(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_LATITUDE));
							e.setLon(rs
									.getFloat(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_LONGITUDE));
							e.setPc_ip(rs
									.getString(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_PC_IP));

							host.setHost_ip(rs
									.getString(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_HOST_IP));
							host.setId(rs
									.getLong(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_HOST_ID));
							host.setName(rs
									.getString(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_HOST_NAME));
							host.setProfile_img(GlobalVariables.EVENT_DATABASE_COLUMN.EVENT_COLUMN_HOST_PROFILE_IMG);

							e.setHost(host);
							list.add(e);
						}
						return list;
					}
				});
		return result;
	}

	
	
	@Override
	public List<EventDO> getAll() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		List<EventDO> eventList = query(map);
		return eventList;
	}

	@Override
	public int clearAll() {
		// TODO Auto-generated method stub
		
		// clear from the database
		String sqlDelete = "truncate table event_table";		
		jdbcTemplateEvent.update(sqlDelete);
		return 0;
	}

}
