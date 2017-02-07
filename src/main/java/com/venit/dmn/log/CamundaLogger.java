package com.venit.dmn.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tamas.Matanyi
 *
 * © Copyright 2014 VeN-IT Ltd.
 * 
 */

public class CamundaLogger {
	
	Logger logger;
	private static CamundaLogger instanceOfClass = null;
	private static FileHandler instanceOfFile = null;
	
	
	protected CamundaLogger() {
	      // Exists only to defeat instantiation.
	}
	
	public static CamundaLogger getInstanceOfClass(String caller) {
		if(instanceOfClass == null) {
			instanceOfClass = new CamundaLogger(caller);
		}
		return instanceOfClass;
	}
	
	
	// Constructor.
	protected CamundaLogger (String caller) {
		
		if (instanceOfFile == null) {
			instanceOfFile = getInstanceOfFile();
			
			this.logger = Logger.getLogger(caller);
			
	        // This block configures the logger with handler and formatter.  
	        logger.addHandler(instanceOfFile);
	        HtmlFormatter formatter = new HtmlFormatter();  
	        instanceOfFile.setFormatter(formatter);  
	        
	        // To avoid appearing log message on console.
//	        logger.setUseParentHandlers(false);
	        logger.setLevel(Level.INFO);
		}
		
		
	}
	
	
	// Gets file handler instance.
	private static FileHandler getInstanceOfFile () {
		FileHandler fh = null;
				
//		if (fh == null) {
			synchronized (CamundaLogger.class) {
				try {
					fh = new FileHandler(LogPath.getLogPath());
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
//		}
		return fh;
	}
	
	
	// Logging new message.
	public void putMessage(Level level, String message) {
		logger.log(level, message);
	}
	
	
	// Closing file handler.
	public void closeHandler() {
//		instanceOfFile.close();
//		instanceOfFile = null;
//		instanceOfClass = null;
	}
	
}