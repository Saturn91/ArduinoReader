package com.saturn91.arduinoReader;

import com.saturn91.logger.Log;

import controller.Controller;

/**
 * Startpoint of programm, main gets called automaticly if the Programm gets startet
 */
public class Main 
{
	
	private static boolean isPi = false;
    public static void main( String[] args )
    {
    	if (args.length > 0) {
    		isPi = args[0].equalsIgnoreCase("pi");
    	}
    	Log.setDebugMode(1);
    	Log.setLogFileDebugModus(10);
    	Log.printLn("---startet Arduino-monitoring---", Main.class.getSimpleName(), 0);
    	new Controller();
    }
    
    public static boolean isPi() {
    	return isPi;
    }
}
