package com.saturn91.arduinoReader.gui.subPanels;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.saturn91.logger.Log;

public class CMD_Panel extends PanelComponent{
	
	private JTextPane cmd_Output;
	private JLabel title;
	private ArrayList<Line> lines = new ArrayList<Line>();
	private ArrayList<Line> newLines = new ArrayList<Line>();
	private int maxLines = 10;

	public CMD_Panel(int x, int y, int width, int height, int maxLines) {
		super(x, y, width, height);
		this.maxLines = maxLines;
	}

	@Override
	public void init() {
		Log.printLn("---starting CMD---", getClass().getSimpleName(), 3);
		setBounds();
		setLayout(null);
		title = new JLabel("Commandline Output");
		cmd_Output = new JTextPane();
		cmd_Output.setBounds(0, 20, getWidth(), getHeight()-25);
		cmd_Output.setBackground(Color.black);
		cmd_Output.setFocusable(false);
		cmd_Output.setAutoscrolls(true);
		add(cmd_Output);
		add(title);
		title.setBounds(0,0, getWidth(), 18);
	}

	@Override
	public void update() {
		if(newLinesAdded){
			for(Line nl: newLines){
				lines.add(nl);
				appendToPane(cmd_Output, nl.getText(), nl.getColor());
			}
			newLines.clear();
			newLinesAdded = false;
		}	
		
		if(lines.size() > maxLines){
			autoscroll();
		}
	}
	
	private void autoscroll() {
		cmd_Output.setText("");
		lines = new ArrayList<Line>(lines.subList(lines.size()-maxLines, lines.size()));
		for(Line l: lines){
			appendToPane(cmd_Output, l.getText(), l.getColor());
		}
	}
	private boolean newLinesAdded = false;
	public void addLine(String text){
		newLinesAdded = true;
		newLines.add(new Line(text, Color.WHITE));
	}
	
	public void addInfoLine(String info){
		newLinesAdded = true;
		newLines.add(new Line(info, Color.YELLOW));
	}
	
	public void addErrorLine(String error){
		newLinesAdded = true;
		newLines.add(new Line(error, Color.RED));
	}
	
	public void clearFirstLine(){
		cmd_Output.setText("");
		if(lines.size() > 0){
			lines = new ArrayList<Line>(lines.subList(1, lines.size()));
			for(Line l: lines){
				appendToPane(cmd_Output, l.getText(), l.getColor());
			}
		}		
	}
	
	private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
	
	private class Line{
		private String text;
		private Color color;
		
		public Line(String text, Color color) {
			super();
			this.text = text;
			this.color = color;
		}

		public String getText() {
			return text;
		}

		public Color getColor() {
			return color;
		}		
	}
}
