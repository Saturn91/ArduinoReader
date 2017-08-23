package com.saturn91.arduinoReader.arduino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import com.saturn91.logger.Log;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * This Class reads the serial connection to the arduino
 * 
 * ArduinoReader was not only programmed by me, a big Part is from a Arduinotutorial located at:
 * https://playground.arduino.cc/Interfacing/Java
 * 
 * @author MGeissbergerWin10
 *
 */

public class ArduinoReader implements SerialPortEventListener{

	private ArrayList<TimeStampedStringMessage> buffer;							//in the buffer messages get stored until they can be processed

	private static final String PORT_NAMES[] = { 			//Portnames on Computer which is connected to the Arduino
			"/dev/tty.usbserial-A9007UX1", 					// Mac OS X
			"/dev/ttyACM0", 								// Raspberry Pi
			"/dev/ttyUSB0", 								// Linux
			"COM3",											// Windows
	};
	
	private SerialPort serialPort;							//Port the Arduino is connected to
	private boolean connectionFailed = false;				//Connect to Arduino failed, normally because the Port is already in use by an other application (like Arduino-Ide)
	
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public ArduinoReader() {
		Log.printLn("---started Arduino Reader---", getClass().getSimpleName(), 3);
		init();
	}
	
	/**
	 * Returns the last messages received on Serial (deletes them after)
	 * @return
	 */
	public ArrayList<TimeStampedStringMessage> getlastMessages() {
		ArrayList<TimeStampedStringMessage> returnvalue = null;
		if(buffer.size() > 0) {
			returnvalue = (ArrayList<TimeStampedStringMessage>) buffer.clone();
			buffer.clear();
		}
		return returnvalue;
	}
	
	public void send(byte msg) {
		if(!connectionFailed) {
			try {
				output.write(msg);
			} catch (IOException e) {
				connectionFailed = true;
				Log.printErrorLn("unexpected Error while sending", getClass().getSimpleName(), 1);
			}
		}else {
			Log.printErrorLn("not able to send: connection failed", getClass().getSimpleName(), 1);
		}
		
	}


	/**
	 * Sets up the connection with the Arduino
	 */
	public void init() {
		
		buffer = new ArrayList<TimeStampedStringMessage>();			//initialize Buffer

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			connectionFailed = true;
			Log.printErrorLn("Could not find COM port.", getClass().getSimpleName(), 1);
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			connectionFailed = true;
			Log.printErrorLn("unecpected Error while opening Serial connection", getClass().getSimpleName(), 1);
			Log.printErrorLn(e.toString(), getClass().getSimpleName(), 1);
			
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public void serialEvent(SerialPortEvent oEvent) {
		if(!connectionFailed) {
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					String inputLine=input.readLine();
					
					//write the inputline (=serial message from Arduino) to the buffer so the programm can process it later
					buffer.add(new TimeStampedStringMessage(System.currentTimeMillis(), inputLine));
					
					//print in Logger (if debugmodus is higher than 5)
					Log.printLn("[SERIAL] : " + inputLine, getClass().getSimpleName(), 6);
					
				} catch (Exception e) {
					Log.printErrorLn("unecpected Error while reading Serial Port", getClass().getSimpleName(), 2);
					Log.printErrorLn(e.toString(), getClass().getSimpleName(), 2);
				}
			}	
		}else {
			Log.printErrorLn("not able to handle SerialPortEvent: connection failed", getClass().getSimpleName(), 1);
		}
		
		
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
}
