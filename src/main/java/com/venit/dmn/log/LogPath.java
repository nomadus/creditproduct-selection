package com.venit.dmn.log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tamas.Matanyi
 *
 * © Copyright 2014 VeN-IT Ltd.
 * 
 */

public class LogPath {
	
	// Getting XML path under apache-tomcat.
	public static String getLogPath() {
		String baseDir = System.getProperty("catalina.base");
		baseDir += "/logs/camundaLog." + calcDate() + ".html";
		return baseDir;
	}
	
	// Get actual date.
	private static String calcDate() {
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
		Date resultdate = new Date();
		return date_format.format(resultdate);
	}
	
}