package com.saturn91.arduinoReader.arduino;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import com.saturn91.logger.Log;

import controller.Controller;

/**
 * This class combines a string message with a timestamp, 
 * this is important if we want to draw data versus the time
 * @author MGeissbergerWin10
 *
 */
public class TimeStampedStringMessage {
	private long timeStamp;
	private String message;
	
	public TimeStampedStringMessage(long timeStamp, String message) {
		long now = System.currentTimeMillis();
		this.timeStamp = now - Controller.getStartTime(); 
		this.message = message;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public String getMessage() {
		return message;
	}	
}
