package org.dartmouth.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dartmouth.common.GlobalVariables;
import org.dartmouth.common.Result;
import org.dartmouth.domain.CacheEventDO;
import org.dartmouth.domain.EventDO;
import org.dartmouth.domain.ParticipantDO;
import org.dartmouth.service.EventService;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yaozhong Kang
 * @date May 21, 2014
 */
@Controller
public class EventController {

	@Autowired
	private EventService eventService;

	static Logger logger = Logger.getLogger(EventController.class.getName());

	@RequestMapping(value = "/create_event")
	public void create_event(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			EventDO event = EventDO.getNewInstance();
			event.fillByRequest(request);

			// Get the host
			String host_id = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_HOST_ID);
			String host_ip = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_HOST_IP);
			String profile_img = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_PROFILE_IMG);
			String name = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_HOST_NAME);

			// Invalid Parameters
			if (event.getEvent_name() == null
					|| event.getEvent_name().length() == 0 || host_id == null
					|| host_id.length() == 0) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
						.endObject();
				response.getWriter().append(stringer.toString());
				return;
			}

			ParticipantDO part = new ParticipantDO();
			part.setHost_ip(host_ip);
			part.setId(Long.valueOf(host_id));
			part.setProfile_img(profile_img);
			part.setName(name);

			event.setHost(part);

			// Add an event to the database
			Result resultDB = this.eventService.addEventToDB(event);

			// Add event to the cache

			if (resultDB.isSuccess()) {

				CacheEventDO cEvent = new CacheEventDO();
				cEvent.setEvent_id((Long) resultDB.getResultObj());
				cEvent.setEvent_time(System.currentTimeMillis());
				cEvent.setHost_id(event.getHost().getId());
				cEvent.setHost_ip(event.getHost().getHost_ip());
				cEvent.setLast_heartbeat_time(System.currentTimeMillis());

				this.eventService.addEventToCache(cEvent);
			}

			stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(resultDB.isSuccess())
					.key(GlobalVariables.RESPONSE_KEYS.EVENTID)
					.value(resultDB.getResultObj()).endObject();

			response.getWriter().append(stringer.toString());

		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/create_event_pc")
	public void create_event_pc(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			EventDO event = EventDO.getNewInstance();
			event.fillByRequest(request);

			// TODO if event is valid
			// event.setEvent_id(EventDO.incrementCount());
			Long host_id = Long.valueOf(request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_HOST_ID));
			String host_ip = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_HOST_IP);
			String profile_img = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_HOST_PROFILE_IMG);
			String name = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_HOST_NAME);

			ParticipantDO part = new ParticipantDO();
			// only difference with
			// part.setHost_ip(host_ip);
			event.setPc_ip(host_ip);
			part.setId(host_id);
			part.setProfile_img(profile_img);
			part.setName(name);
			event.setHost(part);

			// Add event to the database
			Result result = this.eventService.addEventToDB(event);

			if (result.isSuccess()) {

				// Add event to the cache
				CacheEventDO cEvent = new CacheEventDO();
				cEvent.setEvent_id((Long) result.getResultObj());
				cEvent.setEvent_time(System.currentTimeMillis());
				cEvent.setHost_id(event.getHost().getId());
				cEvent.setHost_ip(event.getHost().getHost_ip());
				cEvent.setLast_heartbeat_time(System.currentTimeMillis());
				this.eventService.addEventToCache(cEvent);
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true).key(GlobalVariables.RESPONSE_KEYS.EVENTID)
						.value(event.getEvent_id().toString()).endObject();
				response.getWriter().append(stringer.toString());

			} else {
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.EVENTID).value(-1L)
						.endObject();
				response.getWriter().append(stringer.toString());
			}

		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/discover")
	public void discover(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			String lat = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_LAT);
			String lon = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_LON);
			String zip = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_ZIP_CODE);

			// Invalid parameters : the zip may null, check if the service
			if (lat == null || lon == null || lat.length() == 0
					|| lon.length() == 0) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
						.endObject();
				response.getWriter().append(stringer.toString());
				return;
			}

			PriorityQueue<EventDO> es = this.eventService.getNearByEvents(
					Float.valueOf(lat), Float.valueOf(lon), zip);

			JSONObject element = new JSONObject();
			List<JSONObject> arr = new ArrayList<JSONObject>();

			int count = 0;
			while (!es.isEmpty() && count < GlobalVariables.MAX_NEAR_EVENTS) {
				EventDO ev = es.poll();
				JSONObject ee = new JSONObject();
				ee.put("event_mode", ev.getEvent_mode());
				ee.put("allow_add", ev.getAllow_add());
				ee.put("event_id", ev.getEvent_id());
				ee.put("event_name", ev.getEvent_name());
				ee.put("host_id", ev.getHost().getId().toString());
				ee.put("host_profile_img", ev.getHost().getProfile_img());
				ee.put("host_name", ev.getHost().getName());
				arr.add(ee);
				count++;
			}
			element.accumulate("events", arr.toString());
			stringer.object().key("value").value(element).endObject();
			response.getWriter().append(stringer.toString());
		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/pull_info")
	public void pull_info(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			Long eventId = Long.valueOf(request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_EVENT_ID));
			EventDO event = this.eventService.getEventFromDB(eventId);
			if (event != null) {
				stringer.object().key("event_name")
						.value(event.getEvent_name()).key("event_mode")
						.value(event.getEvent_mode()).key("event_time")
						.value(event.getEvent_time()).key("pc_ip")
						.value(event.getPc_ip()).key("host_id")
						.value(event.getHost().getId()).endObject();
				response.getWriter().append(stringer.toString());
			}
		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/host_ip")
	public void host_ip(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			String eventId = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_EVENT_ID);
			String hostId = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_HOST_ID);

			// Invalid Parameter
			if (eventId == null || hostId == null || eventId.length() == 0
					|| hostId.length() == 0) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
						.endObject();
				response.getWriter().append(stringer.toString());
				return;
			}

			CacheEventDO event = this.eventService.getEventFromCache(Long
					.valueOf(eventId));

			// If the event exists and match the host id
			if (event != null && event.getHost_id() == Long.valueOf(hostId)) {
				stringer.object().key("host_ip").value(event.getHost_ip())
						.endObject();
				response.getWriter().append(stringer.toString());
			} else {
				stringer.object().key("host_ip").value(null).endObject();
				response.getWriter().append(stringer.toString());
			}

		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/leave")
	public void leave(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {
			String eventId = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_EVENT_ID);
			String hostId = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_HOST_ID);

			// Invalid Parameter
			if (eventId == null || hostId == null || eventId.length() == 0
					|| hostId.length() == 0) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
						.endObject();
				response.getWriter().append(stringer.toString());
				return;
			}

			Result resultDB = this.eventService.deleteEventFromDB(
					Long.valueOf(eventId), Long.valueOf(hostId));

			Result resultCache = this.eventService.deleteEventFromCache(
					Long.valueOf(eventId), Long.valueOf(hostId));

			// delete success
			if (resultDB.isSuccess()) {
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true).endObject();
				response.getWriter().append(stringer.toString());

			} else { // delete fails
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false).endObject();
				response.getWriter().append(stringer.toString());
			}

		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

	@RequestMapping(value = "/heart_beat")
	public void heart_beat(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONStringer stringer = new JSONStringer();
		try {

			// Long eventId = Long.valueOf(request.getParameter("event_id"));
			// Long hostId = Long.valueOf(request.getParameter("id"));
			String host_ip = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_USER_IP);
			String eventId = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_EVENT_ID);
			String hostId = request
					.getParameter(GlobalVariables.REQUEST_KEYS.KEY_ID);

			// Invalid Parameters
			if (host_ip == null || eventId == null || hostId == null
					|| host_ip.length() == 0 || eventId.length() == 0
					|| hostId.length() == 0) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.INVALID_PARAMETERS)
						.endObject();
				response.getWriter().append(stringer.toString());
				return;
			}

			CacheEventDO event = this.eventService.getEventFromCache(Long
					.valueOf(eventId));

			if (event == null) {
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.EVENT_NOT_EXIST)
						.endObject();
				response.getWriter().append(stringer.toString());
				return;
			}

			// Event user match
			if (event.getHost_id().equals(Long.valueOf(hostId))) {
				// Reset the ip address
				event.setHost_ip(host_ip);

				// Reset the last heart beat time
				event.setLast_heartbeat_time(System.currentTimeMillis());

				this.eventService.addEventToCache(event);
				stringer.object().key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(true).endObject();
				response.getWriter().append(stringer.toString());
			} else { // Event user not match
				stringer.object()
						.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
						.value(false)
						.key(GlobalVariables.RESPONSE_KEYS.MSG)
						.value(GlobalVariables.RESPONSE_MESSAGES.EVENT_USER_MISMATCH)
						.endObject();
				response.getWriter().append(stringer.toString());
			}

		} catch (Exception e) {
			stringer.object()
					.key(GlobalVariables.RESPONSE_KEYS.SUCCESS)
					.value(false)
					.key(GlobalVariables.RESPONSE_KEYS.MSG)
					.value(GlobalVariables.RESPONSE_MESSAGES.SYSTEM_ERROR)
					.endObject();
			response.getWriter().append(stringer.toString());
		}
	}

}
