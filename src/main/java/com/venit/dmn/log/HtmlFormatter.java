package com.venit.dmn.log;

/**
 * @author Tamas.Matanyi
 *
 * © Copyright 2014 VeN-IT Ltd.
 * 
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

// Custom formatter: formats parts of a log record to a single line
class HtmlFormatter extends Formatter {
	
	
	// This method is called for every log records
	public String format(LogRecord rec) {
		StringBuffer buf = new StringBuffer(1000);
		String colour = "";
		boolean isRed = false;
		
		buf.append("<tr> \n\r");
		
		// Colorize log levels
		if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
			colour = "style=\"color:red\"";
			isRed = true;
		} else if (rec.getLevel().intValue() <= Level.FINE.intValue() || rec.getMessage().startsWith("Result and reference are NOT identical on")) {
			colour = "style=\"color:green\"";
			isRed = false;
		} else {
			colour = "";
			isRed = false;
		}
		
		// Level
		if (isRed) {
			buf.append("\t<td " + colour + ">");
			buf.append("<b>");
			buf.append(rec.getLevel());
			buf.append("</b>");
		} else {
			buf.append("\t<td " + colour + ">");
			buf.append(rec.getLevel());
		}
		buf.append("</td> \n\r");
		
		// Date
		if (isRed) {
			buf.append("\t<td " + colour + ">");
//			buf.append("<b>");
			buf.append(calcDate(rec.getMillis()));
//			buf.append("</b>");
		} else {
			buf.append("\t<td " + colour + ">");
			buf.append(calcDate(rec.getMillis()));
		}
		buf.append("</td> \n\r");
		
		// Message
		if (isRed) {
			buf.append("\t<td " + colour + ">");
			buf.append("<b>");
			buf.append(formatMessage(rec));
			buf.append("</b>");
		} else {
			buf.append("\t<td " + colour + ">");
			buf.append(formatMessage(rec));
		}
		buf.append("</td> \n\r");
		buf.append("</tr> \n\r");
		
		return buf.toString();
	}

	private String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}
	
	
	// This method is called just after the handler using this formatter is created.
	public String getHead(Handler h) {
		return "<!DOCTYPE html>\n\r<html>\n\r<head>\n\r<meta charset=\"UTF-8\">\n\r<title>LOG file</title>"
				+ "</head> \n\r"
				+ "<body> \n\r"
				+ "<h1> Camunda test automation LOG file</h1> \n\r"
				+ "<table border=\"0\" cellpadding=\"5\" cellspacing=\"3\"> \n\r"
				+ "<tr align=\"left\"> \n\r"
				+ "\t<th style=\"width:10%\">Loglevel</th> \n\r"
				+ "\t<th style=\"width:15%\">Time</th> \n\r"
				+ "\t<th style=\"width:75%\">Log Message</th> \n\r"
				+ "</tr> \n\r";
	}
	
	
	// This method is called just after the handler using this formatter is closed.
	public String getTail(Handler h) {
		return "</table>\n\r</body>\n\r</html>";
	}
	
} 