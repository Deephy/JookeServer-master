package org.dartmouth.common;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author Yaozhong Kang
 * @date May 21, 2014
 */
public class CommonUtils {

	/**
	 * A function used to test Serialize an object into an string, output all of
	 * the results of public getter functions
	 * 
	 * @param o
	 * @return
	 */
	public static String testObj2String(Object o, String... name) {
		StringBuffer buffer = new StringBuffer();
		try {
			Class<? extends Object> c = o.getClass();
			Method[] ms = c.getMethods();
			for (Method m : ms) {
				String functionName = m.getName();
				if (functionName.startsWith("get")) {
					Object k = m.invoke(o, new Object[] {});
					buffer.append(functionName.substring(3));
					buffer.append(":");
					buffer.append(k == null ? "null" : k.toString());
					buffer.append(" ");
				}
			}
		} catch (Exception e) {
		}
		return buffer.toString();
	}

	public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (float) (dist * meterConversion);
	}
	
	
	
	/* Hash a string to md5 in the dex format*/

	public static String generateMD5(String str) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		
		return sb.toString();
	}
}
