package com.saturn91.arduinoReader.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.saturn91.arduinoReader.gui.subPanels.CMD_Panel;
import com.saturn91.arduinoReader.gui.subPanels.GraphPanel;
import com.saturn91.arduinoReader.gui.subPanels.PanelComponent;
import com.saturn91.arduinoReader.gui.subPanels.SettingPanel;

import controller.Controller;
import controller.GraphData;

/**
 * CostumPanel
 * this class will hold all the physikly existing GUI elements such as labels and graphs, only used 
 * to display objects
 * 
 * @author M.Geissbberger
 *
 */

public class MainPanel extends PanelComponent{
	
	private static final long serialVersionUID = 1L;
	
	private GraphPanel graphPanel;
	private SettingPanel settingPanel;
	private CMD_Panel cmd_Panel;
	
	public static final int border = 10;
	public static final int defaultbtnWidth = 150;
	public static final int defaultbtnHeight = 30;
	public static final int defaultTextFieldHeight = 75;

	public MainPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		setLayout(null);
	}
	
	public void init(){
		graphPanel = new GraphPanel(0, 0, getWidth()-2*border, getHeight()*2/3);
		settingPanel = new SettingPanel(0,  getHeight()*2/3, (getWidth()-4*border)/3, getHeight()/3);
		cmd_Panel = new CMD_Panel(getWidth()/3, getHeight()*2/3, (getWidth())*2/3-2*border, getHeight()/3-5*border, 20);	//should be: 900, 540, 450, 360, (22 = maxLines)
		add(graphPanel);
		graphPanel.init();
		add(settingPanel);
		settingPanel.init();
		add(cmd_Panel);
		cmd_Panel.init();
	}
	
	public void resize() {
		graphPanel.setBounds(0, 0, getWidth(), getHeight()*2/3);
		cmd_Panel.setBounds(getWidth()/3, getHeight()*2/3, (getWidth()-2*border)*2/3, (getHeight()-border)/3);
	}
	
	public void setData(Controller c){
		//graphPanel.setData();
	}

	@Override
	public void update() {
		graphPanel.update();
		settingPanel.update();
		cmd_Panel.update();
	}	
	
	public void addCMD_Line(String line){
		cmd_Panel.addLine(line);
	}
	
	public void addCMD_Error_Line(String error){
		cmd_Panel.addErrorLine(error);
	}
	
	public void addCMD_Info_Line(String info){
		cmd_Panel.addInfoLine(info);
	}

	public void clearGraphs() {
		graphPanel.clear();		
	}

	public void addPointToGraph(GraphData data) {
		graphPanel.addPointToGraph(data);
		
	}

	public SettingPanel getSettingPanel() {
		return settingPanel;
	}
	
	public GraphPanel getGraphPanel() {
		return graphPanel;
	}
}
