package com.saturn91.arduinoReader.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.saturn91.arduinoReader.gui.subPanels.GraphPanel;
import com.saturn91.arduinoReader.gui.subPanels.LogTypes;
import com.saturn91.arduinoReader.gui.subPanels.SettingPanel;
import com.saturn91.logger.Log;

import controller.Controller;
import controller.GraphData;

/**
 * This class is the graphical interface of the programm. It only displays. 
 * It gets controlled by the Controller Class
 * @author MGeissbergerWin10
 */
public class GUI extends JFrame{

	//--------------Swing needs a serialnumber--------
	private static final long serialVersionUID = 1L;

	//------------Window Config------------
	private static final String title = "Visual-Plot of Arduino Signals";

		//--default windowsize
	private static int width = 1350;
	private static int height = 950;
	private static int xPos = 50;
	private static int yPos = 50;
	//-----------\Window Config-------------

	//Components
	private static MainPanel panel;

	//--------------Constructor-----------------------
	public GUI(){
		super();
		Log.printLn("----Starting GUI----", getClass().getSimpleName(), 0);
		init();

	}

	public void init(){
		//get Screensize to make programm as big as possible
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth();
		height = (int) screenSize.getHeight();
		xPos = 0;
		yPos = 0;
		
		setTitle(title);
		setSize(width,height);
		setLocation(xPos, yPos);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	//gets handled in a window Listener!
		setResizable(false);
		panel = new MainPanel(0, 0, width, height);
		add(panel);
		panel.init();
		panel.setBounds();
		setVisible(true);

		//Overwrite default close operation
		addWindowListener(new WindowAdapter() {		
			@Override
			public void windowClosing(WindowEvent e) {
				Controller.stop();
				Log.printLn("Window closed", "GUI", 0);
			}			
		});
	}

	public void update() {
		String[] logmessages = Log.getLastMessages().split("\n");
		if(logmessages.length > 0) {
			if(!logmessages[0].isEmpty()) {
				for(String s: logmessages) {
					if(s.contains("ERROR")) {
						addCMDLine(LogTypes.ERROR, s);
					}else {
						addCMDLine(LogTypes.NORMAL, s);
					}
				}
			}
		}	
		panel.update();	
		changeChanel();
	}

	private void addCMDLine(LogTypes type, String message) {
		switch(type) {
		case ERROR:
			panel.addCMD_Error_Line(message+ "\n");
			break;
		case NORMAL:
			panel.addCMD_Line(message+ "\n");
			break;
		}		
	}

	
	public static int getGUIWidth() {
		return width;
	}

	public static int getGUIHeight() {
		return height-40;
	}

	public static int getGUIxPos() {
		return xPos;
	}

	public static int getGUIyPos() {
		return yPos;
	}	
	
	public void addPointToGraph(GraphData data) {
		panel.addPointToGraph(data);
	}
	
	public SettingPanel getSeettingPanel() {
		return panel.getSettingPanel();
	}
	
	private void changeChanel() {
		if(getSeettingPanel().getChangeChanelFlag()) {
			if(getSeettingPanel().getAllChannels()) {
				panel.getGraphPanel().setRecordingChanel(getSeettingPanel().getChanelRecord());
				panel.getGraphPanel().setRecordonlyOneChanel(false);
			}else {
				panel.getGraphPanel().setRecordingChanel(getSeettingPanel().getChanelRecord());
				panel.getGraphPanel().setRecordonlyOneChanel(true);
				
			}
			panel.getGraphPanel().init();
		}
	}
}
