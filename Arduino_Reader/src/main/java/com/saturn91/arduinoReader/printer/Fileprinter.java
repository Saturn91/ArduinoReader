package com.saturn91.arduinoReader.printer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.saturn91.logger.Log;

public class Fileprinter {

	public static void printTxt(String string, String path) {
		try(  PrintWriter out = new PrintWriter(path)  ){
			out.println(string);
			Log.printLn("Printed File: " + path, Fileprinter.class.getSimpleName(), 5);
		} catch (FileNotFoundException e) {
			Log.printErrorLn("not able to save File!", Log.class.getName(), 1);
			e.printStackTrace();
		}	
	}
}
