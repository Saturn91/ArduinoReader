package controller;

public class GraphData {
	private int channel;
	private float xValue;
	private float yValue;
	
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
}
