package com.saturn91.arduinoReader.gui.subPanels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.saturn91.logger.Log;

public class SettingPanel extends PanelComponent{

	private JTextField logDepth;
	private JTextField chanel;

	private boolean saveData = true;
	private boolean saveNowFlag = false;

	private int chanelRecord = 0;
	private boolean changeChanelFlag = false;
	private boolean allChanels = true;

	public SettingPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		setLayout(null);
		setBounds();	
	}

	@Override
	public void init() {
		JCheckBox saveDataBox = new JCheckBox();
		saveDataBox.setSelected(saveData);
		JLabel saveDataLabel = new JLabel("Save Data: ");
		add(saveDataBox);
		add(saveDataLabel);
		saveDataBox.setBounds(160, 10, 20, 20);
		saveDataLabel.setBounds(10, 10, 150, 20);

		saveDataBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
					saveData = true;
					Log.printLn("saving Data from now on", getClass().getSimpleName(), 1);
				} else {//checkbox has been deselected
					Log.printLn("Not longer saving Data", getClass().getSimpleName(), 1);
					saveData = false;
				};				
			}
		});

		JButton saveNow = new JButton("Save Data now");
		saveNow.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				saveNowFlag = true;				
			}
		});
		saveNow.setBounds(10, 40, 150, 30);
		add(saveNow);

		logDepth = new JTextField(Log.getDebugMode());
		logDepth.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					int num = Integer.parseInt(logDepth.getText());
					Log.setDebugMode(num);
				} catch (Exception e2) {
					Log.printErrorLn("Log depth must be an Integer", getClass().getSimpleName(), 1);
					logDepth.setText("NAN");
				}				
			}
		});

		logDepth.setBounds(10, 80, 150, 30);
		add(logDepth);

		chanel = new JTextField("0");
		chanel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int num = Integer.parseInt(chanel.getText());
					chanelRecord = num;
					if(num < 0) {
						Log.printErrorLn("chanel must be a positive Integer!", getClass().getSimpleName(), 1);
					}
				} catch (Exception e2) {
					chanel.setText("NAN");
					Log.printErrorLn("chanel must be a positive Integer!", getClass().getSimpleName(), 1);
				}				
			}
		});

		chanel.setBounds(10, 120, 40, 30);
		add(chanel);

		JButton changeChanelBtn = new JButton("start");
		changeChanelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeChanelFlag = true;
				try {
					int num = Integer.parseInt(chanel.getText());
					chanelRecord = num;
					if(num < 0) {
						Log.printErrorLn("chanel must be a positive Integer!", getClass().getSimpleName(), 1);
					}
				} catch (Exception e2) {
					chanel.setText("NAN");
					Log.printErrorLn("chanel must be a positive Integer!", getClass().getSimpleName(), 1);
				}
			}
		});

		changeChanelBtn.setBounds(60, 120, 100, 30);
		add(changeChanelBtn);

		JCheckBox recordAllChannels = new JCheckBox();
		recordAllChannels.setSelected(allChanels);
		JLabel recordAllChannelsLabel = new JLabel("Record all Data");
		add(recordAllChannels);
		add(recordAllChannelsLabel);
		recordAllChannels.setBounds(170, 120, 50, 20);
		recordAllChannelsLabel.setBounds(220, 120, 150, 20);

		recordAllChannels.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
					allChanels = true;
					Log.printLn("recording all chanels", getClass().getSimpleName(), 1);
				} else {//checkbox has been deselected
					Log.printLn("recording only one chanel!", getClass().getSimpleName(), 1);
					allChanels = false;
				};				
			}
		});
	}

	@Override
	public void update() {

	}

	public boolean getSaveData() {
		return saveData;
	}

	public boolean getSaveNowFlag() {
		boolean returnValue = saveNowFlag;
		saveNowFlag = false;
		return returnValue;
	}

	public int getChanelRecord() {
		return chanelRecord;
	}

	public boolean getChangeChanelFlag() {
		boolean flag = changeChanelFlag;
		changeChanelFlag = false;
		return flag;
	}
	
	public boolean getAllChannels() {
		return allChanels;
	}
}
