package org.dartmouth.domain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dartmouth.common.CommonUtils;

/**
 * @author Yaozhong Kang
 * @date May 21, 2014
 */
public class BaseDO implements Serializable {
	private static final long serialVersionUID = 7030898311155197479L;

	/**
	 * automatically set the attributes value according to the map. supported
	 * types: Integer, Boolean, Float, Long, String. Other types need to be set
	 * manually.
	 * 
	 * @param map
	 */
	public void fillByMap(Map<String, String> map) {
		Class<? extends BaseDO> bc = this.getClass();
		Field[] fields = bc.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			try {
				Object obj = map.get(f.getName());
				if (obj == null) {
					continue;
				}
				String value = String.valueOf(obj);

				if (f.getType() == Integer.class) {
					f.set(this, Integer.valueOf(value));
				} else if (f.getType() == Boolean.class) {
					f.set(this, Boolean.valueOf(value));
				} else if (f.getType() == String.class) {
					f.set(this, value);
				} else if (f.getType() == Float.class) {
					f.set(this, Float.valueOf(value));
				} else if (f.getType() == Long.class) {
					f.set(this, Long.valueOf(value));
				} else {
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				f.setAccessible(false);
			}
		}
	}

	public void fillByRequest(HttpServletRequest request) {
		Class<? extends BaseDO> bc = this.getClass();
		Field[] fields = bc.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			try {
				String value = request.getParameter(f.getName());
				if (value == null) {
					continue;
				}
				if (f.getType() == Integer.class) {
					f.set(this, Integer.valueOf(value));
				} else if (f.getType() == Boolean.class) {
					f.set(this, Boolean.valueOf(value));
				} else if (f.getType() == String.class) {
					f.set(this, value);
				} else if (f.getType() == Float.class) {
					f.set(this, Float.valueOf(value));
				} else if (f.getType() == Long.class) {
					f.set(this, Long.valueOf(value));
				} else {
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				f.setAccessible(false);
			}
		}
	}

	// test the functionality
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("signup_type", "100");
		map.put("fullname", "name");
		map.put("id", "5555");
		map.put("gender", "false");
		UserDO user = new UserDO();
		user.fillByMap(map);
		System.out.println(CommonUtils.testObj2String(user));
	}
}
