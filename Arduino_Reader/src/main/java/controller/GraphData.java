package controller;

import java.util.ArrayList;

import com.saturn91.logger.Log;

public class GraphData {
	private int channel;
	private float xValue;
	private float yValue;
	private static ArrayList<String> channelNames = new ArrayList<>();
	
	public GraphData(int channel, float xValue, float yValue) {
		this.channel = channel;
		this.xValue = xValue;
		this.yValue = yValue;
	}

	public int getChannel() {
		return channel;
	}

	public float getxValue() {
		return xValue;
	}

	public float getyValue() {
		return yValue;
	}	
	
	public static void setName(int id, String channelName) {
		if(id >= 0) {
			if(channelNames.size() > id) {
				channelNames.set(id, channelName);
			}else {
				if(channelNames.size() == id) {
					channelNames.add(channelName);
				}else {
					Log.printErrorLn("unecpected Error setName()", GraphData.class.getSimpleName(), 1);
				}				
			}
		}else {
			Log.printErrorLn("channelID can't be less than 0!", GraphData.class.getSimpleName(), 1);
		}		
	}
	
	public static String getChannelName(int id) {
		if(id < channelNames.size() && id >= 0) {
			return channelNames.get(id);
		}else {
			Log.printErrorLn("no channel [" + id + "] got named yet", GraphData.class.getClass().getSimpleName(), 1);
			return "";
		}
		
	}
}
