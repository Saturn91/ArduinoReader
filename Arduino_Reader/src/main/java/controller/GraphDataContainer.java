package controller;

import java.util.ArrayList;

public class GraphDataContainer {
	private ArrayList<GraphData> list;
	
	public GraphDataContainer() {
		list = new ArrayList<GraphData>();
	}
	
	public void addDataPoint(GraphData data) {
		list.add(data);
	}
	
	public ArrayList<GraphData> getChanelData(int chanel){
		ArrayList<GraphData> chanellist = new ArrayList<GraphData>();
		for(GraphData d: list) {
			if(d.getChannel() == chanel) {
				chanellist.add(d);
			}
		}		
		return chanellist;
	}
}
