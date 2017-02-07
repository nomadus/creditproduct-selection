package com.venit.dmn.xml;

/**
 * @author Tamas.Matanyi
 *
 * © Copyright 2014 VeN-IT Ltd.
 * 
 */

public class XMLPath {
	
	// Getting XML path under apache-tomcat.
	public static String getXMLPath(String id) {
		String baseDir = System.getProperty("catalina.base");
		if (!id.equals("")) {
			baseDir += "/XML/" + id + "/"; 
		} else {
			baseDir += "/XML/";
		}
		
		return baseDir;
	}
	
}