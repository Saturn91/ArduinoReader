package controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sound.sampled.DataLine;

import com.saturn91.arduinoReader.arduino.ArduinoReader;
import com.saturn91.arduinoReader.arduino.TimeStampedStringMessage;
import com.saturn91.arduinoReader.gui.GUI;
import com.saturn91.arduinoReader.gui.subPanels.GraphPanel;
import com.saturn91.arduinoReader.gui.subPanels.LogTypes;
import com.saturn91.arduinoReader.printer.Fileprinter;
import com.saturn91.logger.Log;

/**
 * Controller - controls the whole programm
 * Here is the Mainloop of the Programm
 * @author MGeissbergerWin10
 *
 */
public class Controller{



	private static volatile boolean run = true;					//volatile means its the same variable on all Threads!

	//*******************Components*****************************************************************
	private GUI gui;											//grafical user interface
	private ArduinoReader serialCom;							//interface to communicate with arduino

	//*******************Tickline- Variables********************************************************
	private static final float minTimeBetweenUpdateMS = 5;		//The program needs less RAM if it doesen't loop as fast as possible!

	private static long startTime;								//last Time the update methode started (ended but its almost the same time)
	private static long longestDelta = 0;						//longest delta
	private static long lastTicTime = 0;						//time when the last Ticline was shown

	private static final long ticLineRythm = 1000;				//every second the ticline gets printed
	private static final long programmToSlowIfTimeUnder = 25;	//if time between 2 updates gets lower a wrning is fired all ticLineRythm
	//*******************/Tickline-Variables*********************************************************

	//*******************Settings********************************************************************
	private static final long saveDataToFileRythS = 60;			//all 10s the Data gets saved in seperate Files (if checkbox "save Data" is checkd!)
	private static long lastSave;								//the time in ms when the last Save was made
	private static int chanelNum = 7;							//the number of how many Arduinochanels get observed

	//*******************All Graphdata***************************************************************
	private static GraphDataContainer allData;					//The Time in ms when the programm was started

	private static long programStartTime;					
	public Controller() {
		init();
		startTime = System.currentTimeMillis();
		lastTicTime = System.currentTimeMillis();
		while(run) {
			update();
		}
		Log.printLogFile("PeltierTest.txt");
		stopProgramms();
		System.exit(0);
	}

	/**
	 * Init all programms
	 */
	private void init() {
		programStartTime = System.currentTimeMillis();
		allData = new GraphDataContainer();
		gui = new GUI();
		serialCom = new ArduinoReader();
	}

	/**
	 * gets called every 20ms
	 */
	private void update() {
		startTime = System.currentTimeMillis();
		gui.update();

		readoutArduino();
		printData();		
		slowProgrammDown();
	}

	private void readoutArduino() {
		ArrayList<TimeStampedStringMessage> msgs = serialCom.getlastMessages();
		if(msgs != null) {
			for(TimeStampedStringMessage m: msgs) {
				if(m.getMessage().contains("$") && m.getMessage().contains("#")) {
					String[] stringValues = m.getMessage().replace("$", "").replace("#", "").split("_");
					GraphData[] dataList = new GraphData[stringValues.length];
					boolean[] corruptedPoint = new boolean[stringValues.length];

					for(int i = 0; i < stringValues.length; i++) {
						try {
							String[] splitStrings = stringValues[i].split(":");
							float ynum = Float.parseFloat(splitStrings[0]);
							//xvalue will be the time when the message was received!
							float xnum = ((float) m.getTimeStamp())/1000;
							dataList[i] = new GraphData(i, xnum, ynum);
							if(splitStrings.length > 1) {
								GraphData.setName(i, splitStrings[1]);
							}else {
								GraphData.setName(i, "channel" + i);
							}								
						} catch (Exception e) {
							Log.printErrorLn("corupted Data[" + i + "] in:" + m.getMessage(), getClass().getSimpleName(), 1);
							corruptedPoint[i] = true;
						}
					}

					for(int i = 0; i < dataList.length; i++) {
						if(!corruptedPoint[i]) {
							allData.addDataPoint(dataList[i]);
							gui.addPointToGraph(dataList[i]);
						}						
					}				
				}				
			}
		}
	}

	private void printData() {
		boolean saveNow = gui.getSeettingPanel().getSaveNowFlag();
		if(System.currentTimeMillis()-lastSave > saveDataToFileRythS*1000 || saveNow) {
			Log.printLn("Save Data", getClass().getSimpleName(), 1);
			if(gui.getSeettingPanel().getSaveData()) {
				String[] strings = new String[chanelNum];
				StringBuilder sb = new StringBuilder();
				for(int i= 0; i < chanelNum; i++) {
					sb.setLength(0);
					if(allData.getChanelData(i).size() > 0) {
						ArrayList<GraphData> data =  allData.getChanelData(i);
						for(GraphData d: data) {
							sb.append(d.getxValue() + "\t" + d.getyValue() + "\n");
						}
						if(data.size() != 0) {
							Fileprinter.printTxt(sb.toString(), GraphData.getChannelName(i) + ".txt");
						}
					}								
				}
			}	
			if(!saveNow) {
				lastSave = System.currentTimeMillis();
			}			
		}
	}

	/**
	 * Close Threads and stuff in other programmparts
	 */
	private static void stopProgramms() {

	}

	/**
	 * Stop the Controller mainloop (and programm)
	 */
	public static void stop(){
		Log.printLn("---Stopping Mainloop and ending Programm!---", Controller.class.getSimpleName(), 0);
		run = false;
	}

	private static boolean toSlowWarning = false;			//true if the programm gets to slow
	private static int tickCounter = 0;						//counts update cyles

	/**
	 * ensures that the programm sleeps between the different steps
	 * This methode prints informations about the speed the program runs
	 */
	private void slowProgrammDown() {
		long delta = System.currentTimeMillis() - startTime;
		tickCounter++;
		//slow the programm down (computer needs less energy)
		try {
			if( delta < minTimeBetweenUpdateMS) {
				long wait = (long) (minTimeBetweenUpdateMS - (System.currentTimeMillis() - startTime));
				Thread.sleep((long) wait);
			}else {
				if(delta > programmToSlowIfTimeUnder) {
					toSlowWarning = true;
				}
			}
		} catch (Exception e) {
			Log.printErrorLn("unexpected Error while Thread was trying to sleep", Controller.class.getSimpleName(), 1);
			e.printStackTrace();
		}

		if(delta > longestDelta) {
			longestDelta = delta;
		}

		if(System.currentTimeMillis()-lastTicTime >= ticLineRythm) {
			Log.printLn("Ticks: " + tickCounter + ", longest update = " + longestDelta + "ms", Controller.class.getSimpleName(), 7);
			if(toSlowWarning) {
				toSlowWarning = false;
				if(ticLineRythm/tickCounter > 22) {
					Log.printErrorLn("Programm seems to be slow! delta T = " + longestDelta + "ms", Controller.class.getSimpleName(), 1);
				}	
			}
			tickCounter = 0;
			longestDelta = 0;
			lastTicTime = System.currentTimeMillis();
		}
	}

	public static long getStartTime() {
		return programStartTime;
	}
}
