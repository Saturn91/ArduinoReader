package com.saturn91.arduinoReader.gui.subPanels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.saturn91.arduinoReader.gui.MainPanel;
import com.saturn91.jgraph.JGraph;

import com.saturn91.logger.Log;

import controller.Controller;
import controller.GraphData;

public class GraphPanel extends PanelComponent{

	private static final long serialVersionUID = 1L;

	private JGraph valueGraph;

	private int width;
	private int height;

	private int positionMinMaxBox_Y_axis_X = 10;
	private int positionMinMaxBox_Y_axis_Y = 10;

	private int positionMinMaxBox_X_axis_X = 70;
	private int positionMinMaxBox_X_axis_Y;
	
	private int recordingChanel = 0;
	private boolean recordOnlyOneChannel = false;
	
	private static long graphStartTime = System.currentTimeMillis();

	public GraphPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.width = width;
		this.height = height;	
		positionMinMaxBox_X_axis_Y = height-40;
	}

	@Override
	public void init() {
		setLayout(null);
		setBounds();
		initGraphs();	
		initMaxMinLine();
		initBackgrounds();
	}

	private void initMaxMinLine() {
		//***************************Y-Axis Settings************************************************
		int cursorX = positionMinMaxBox_Y_axis_X;
		int cursorY = positionMinMaxBox_Y_axis_Y;

		JButton yUp = new JButton("^");
		yUp.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				valueGraph.shiftUp(1);				
			}
		});

		yUp.setBounds(cursorX, cursorY, MainPanel.defaultbtnHeight+20, MainPanel.defaultbtnWidth);
		add(yUp);

		cursorX = positionMinMaxBox_Y_axis_X;
		cursorY = height-20-MainPanel.defaultbtnHeight-MainPanel.defaultbtnWidth;

		JButton yDown = new JButton("v");
		yDown.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				valueGraph.shiftDown(1);				
			}
		});
		yDown.setBounds(cursorX, cursorY, MainPanel.defaultbtnHeight+20, MainPanel.defaultbtnWidth);
		add(yDown);

		//Zoom for Y-Axis

		cursorX = 10;
		cursorY = ((height-30-MainPanel.defaultbtnHeight)-MainPanel.defaultbtnHeight)/2;

		final JTextField yzoom = new JTextField();
		yzoom.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				float num = -1;
				try {
					float test = Float.parseFloat(yzoom.getText());
					num = test;
				} catch (Exception e2) {
					//Do nothing
				}

				if(num > 0) {
					valueGraph.zoomY(num);
				}else {
					Log.printErrorLn("Y-Zoom must be a positive float", getClass().getSimpleName(), 1);
				}
			}
		});

		yzoom.setBounds(cursorX, cursorY, 50, MainPanel.defaultbtnHeight);
		add(yzoom);


		//***************************X-Axis Settings************************************************
		cursorX = positionMinMaxBox_X_axis_X;
		cursorY = positionMinMaxBox_X_axis_Y;

		JButton xLeft = new JButton("<");
		xLeft.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				valueGraph.shiftleft(1);				
			}
		});
		xLeft.setBounds(cursorX, cursorY, MainPanel.defaultbtnWidth, MainPanel.defaultbtnHeight);
		add(xLeft);

		cursorX = positionMinMaxBox_X_axis_X+(width-25)-40-MainPanel.defaultbtnWidth;
		cursorY = positionMinMaxBox_X_axis_Y;

		JButton xRight = new JButton(">");
		xRight.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				valueGraph.shiftRight(1);				
			}
		});
		xRight.setBounds(cursorX, cursorY, MainPanel.defaultbtnWidth, MainPanel.defaultbtnHeight);
		add(xRight);

		//Zoom for Y-Axis

		cursorX = ((positionMinMaxBox_X_axis_X+(width-25)-40)-50)/2;

		final JTextField xzoom = new JTextField();
		xzoom.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				float num = -1;
				try {
					float test = Float.parseFloat(xzoom.getText());
					num = test;
				} catch (Exception e2) {
					//Do nothing
				}

				if(num > 0) {
					valueGraph.zoomX(num);
				}else {
					Log.printErrorLn("X-Zoom must be a positive float", getClass().getSimpleName(), 1);
				}
			}
		});

		xzoom.setBounds(cursorX, cursorY, 50, MainPanel.defaultbtnHeight);
		add(xzoom);

	}

	private void initBackgrounds(){
		setBackground(Color.GRAY);
		JPanel backgroundX = new JPanel();
		backgroundX.setBounds(positionMinMaxBox_X_axis_X, positionMinMaxBox_X_axis_Y, width-40-positionMinMaxBox_X_axis_X, MainPanel.defaultbtnHeight);
		backgroundX.setBackground(Color.WHITE);
		add(backgroundX);

		JPanel backgroundY = new JPanel();
		backgroundY.setBounds(positionMinMaxBox_Y_axis_X, positionMinMaxBox_Y_axis_Y, MainPanel.defaultbtnHeight+20, height-30-MainPanel.defaultbtnHeight);
		backgroundY.setBackground(Color.WHITE);
		add(backgroundY);

		JPanel seperator = new JPanel();
		seperator.setBackground(Color.GRAY);
		seperator.setBounds(0, height-4, width, 4);
		add(seperator);
	}

	private void initGraphs(){
		reset();
		valueGraph = new JGraph("Values", 70, 10, (width)-65, height-60);
		valueGraph.setBorder(50);
		valueGraph.setMinValue(0, 0);
		valueGraph.setMaxValue(60, 5);
		valueGraph.setxSeperator(10);
		valueGraph.setySeperator(10);
		valueGraph.setGraphNameTextSize(12);
		valueGraph.setxAxisText("[min]");
		valueGraph.setyAxisText("[value]");
		valueGraph.setArrowSize(5);
		valueGraph.setShowDots(false);	
		valueGraph.setRound(4);
		valueGraph.setGraphTextBorder(300);
		add(valueGraph);
	}

	@Override
	public void update() {
		valueGraph.update();		
	}

	/**
	 * Repaint Graphs for new Data
	 */
	public void clear(){
		valueGraph.clear();
	}
	
	public void addData(ArrayList<GraphData> data) {
		for(GraphData d: data) {
			valueGraph.addPoint(d.getChannel(), d.getxValue(), d.getyValue());
			valueGraph.addGraphName(d.getChannel(), "Channel1: " + d.getChannel());
		}
	}

	public void addPointToGraph(GraphData data) {
		if(data.getChannel() == recordingChanel) {
			valueGraph.addPoint(data.getChannel(), data.getxValue()-graphStartTime, data.getyValue());
			valueGraph.addGraphName(data.getChannel(), GraphData.getChannelName(data.getChannel()));
			valueGraph.update();
		}else {
			if(!recordOnlyOneChannel) {
				valueGraph.addPoint(data.getChannel(), data.getxValue()-graphStartTime, data.getyValue());
				valueGraph.addGraphName(data.getChannel(), GraphData.getChannelName(data.getChannel()));
				valueGraph.update();
			}			
		}
		
	}
	
	public void setRecordingChanel(int chanel) {
		recordingChanel = chanel;
	}
	
	public void setRecordonlyOneChanel(boolean flag) {
		recordOnlyOneChannel = flag;
	}
	
	public void reset() {
		graphStartTime = (System.currentTimeMillis()-Controller.getStartTime())/1000;
	}
}
