package com.saturn91.arduinoReader;

import com.saturn91.logger.Log;

import controller.Controller;

/**
 * Startpoint of programm, main gets called automaticly if the Programm gets startet
 */
public class Main 
{
    public static void main( String[] args )
    {
    	Log.setDebugMode(1);
    	Log.setLogFileDebugModus(10);
    	Log.printLn("---startet Peltier-monitoring---", Main.class.getSimpleName(), 0);
    	new Controller();
    }
}
