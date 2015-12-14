import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

@SuppressWarnings("serial")
public class Graph extends JPanel{
	private Vector<Data> theList;
	private Vector<Data> listToTrace;
	private Vector<Data> listMeanValues = new Vector<Data>();
	private int height;
	private int width;
	private int valuesToStore = 50; 
	private int intervalY;
	private int intervalX;
	private int leftBorder = 20;
	private int topBorder = 20;
	private int graduation = 5; //length of a graduation
	
	public Graph(Vector<Data> aList, int height, int width){
		if (aList.size() >= 50){
			theList = (Vector<Data>) aList.subList(aList.size()-valuesToStore, aList.size());
		}
		else {
			theList = aList;
		}
		this.height = height;
		this.width =width;
		System.out.println("A graph has been created");
		this.setBackground(Color.WHITE); //set the color of the graph
	}
	
	//min value of the list to trace
	public float setMin(){
		float min = listToTrace.get(0).getTemperature();
		for (int i=0; i<listToTrace.size(); i++){
			if (listToTrace.get(i).getTemperature() < min){ min = listToTrace.get(i).getTemperature();}
		}
		return min;
	}
	
	//max value of the list to trace
	public float setMax(){
		float max = listToTrace.get(0).getTemperature();
		for (int i=0; i<listToTrace.size(); i++){
			if (listToTrace.get(i).getTemperature() > max){ max = listToTrace.get(i).getTemperature();}
		}
		return max;
	}
	
	//set the scale for the X and Y axis
	public void setInterval(){
		intervalX = width / valuesToStore;
		intervalY = height / (int) (this.setMax()*1.1f); //this.setMax()+10%
	}
	
	public void displayAxis(Graphics g){
		g.drawLine(leftBorder, height+topBorder, width+leftBorder, height+topBorder); //X axis
		g.drawLine(leftBorder, topBorder, leftBorder, height+topBorder); //Y axis
		int maxX = valuesToStore;
		int maxY = (int) (this.setMax()*1.1f);
		for (int i=1; i < maxX; i++){
			g.drawLine((i*intervalX)+leftBorder, height+topBorder, (i*intervalX)+leftBorder, height+topBorder+graduation); //graduation of the X axis
		}
		for (int i=1; i <= maxY; i++){ 
			g.drawLine(leftBorder-graduation, (height+topBorder)-i*intervalY, leftBorder, (height+topBorder)-i*intervalY); //graduation of the Y axis
			g.drawString(Integer.toString(i), 5, (height+topBorder)-i*intervalY);
		}
	}
	
	public void displayGrid(Graphics g){
		int maxX = valuesToStore;
		int maxY = (int) (this.setMax()*1.1f);
		
		g.setColor(Color.LIGHT_GRAY);
		for (int i=1; i < maxX; i++){
			g.drawLine((i*intervalX)+leftBorder, topBorder, (i*intervalX)+leftBorder, height+topBorder); //grid of the X axis
		}
		for (int i=1; i <= maxY; i++){ 
			g.drawLine(leftBorder, (height+topBorder)-i*intervalY, width+leftBorder, (height+topBorder)-i*intervalY); //grid of the Y axis
		}
	}
	
	public void displayMinMax(Graphics g){
		float max = this.setMax(); //max of the list to trace
		float min = this.setMin(); //min of the list to trace
		
		g.setColor(Color.RED);
		g.drawLine(leftBorder, (height+topBorder)-(int)(max*intervalY), width+leftBorder, (height+topBorder)-(int)(max*intervalY));
		g.drawString(Float.toString(max), valuesToStore*intervalX+leftBorder, (height+topBorder) - (int)(max*intervalY));
		g.setColor(Color.GREEN);
		g.drawLine(leftBorder, (height+topBorder)-(int)(min*intervalY), width+leftBorder, (height+topBorder)-(int)(min*intervalY));
		g.drawString(Float.toString(min), valuesToStore*intervalX+leftBorder, (height+topBorder) - (int)(min*intervalY));
	}
	
	public void setListToTrace(){
		if (theList.size() > valuesToStore){
			listToTrace = new Vector<Data>(theList.subList(theList.size()-valuesToStore, theList.size()));
		}
		if (theList.size() <= valuesToStore){
			listToTrace = theList;
		}
	}
	
	public void setListMeanValues(){
		Data theData = new Data();
		float meanTemperature = 0;
		String Date = listToTrace.lastElement().getDate();
		for(int i=0; i<listToTrace.size(); i++){
			meanTemperature += listToTrace.get(i).getTemperature();
		}
		meanTemperature = meanTemperature/listToTrace.size();
		
		theData.setTemperature(meanTemperature);
		theData.setDate(Date);
		if (listMeanValues.size()>valuesToStore){
			listMeanValues.remove(0);
		}
		listMeanValues.add(theData);
	}
	
	public void paint(Graphics g){
		this.setListToTrace();
		
		if (listToTrace.size() > 0){
			this.setInterval();
			this.displayAxis(g);
			this.displayGrid(g);
			this.displayMinMax(g);
			g.setColor(Color.BLUE);
			for (int i=0; i < listToTrace.size()-1; i++){
				g.drawLine(i*intervalX+leftBorder, (height+topBorder) - (int)(listToTrace.get(i).getTemperature()*intervalY), (i+1)*intervalX+leftBorder, (height+topBorder) - (int)(listToTrace.get((i+1)).getTemperature()*intervalY));
			}
			g.drawString(Float.toString(listToTrace.lastElement().getTemperature()), listToTrace.size()*intervalX+leftBorder, (height+topBorder) - (int)(listToTrace.lastElement().getTemperature()*intervalY));
		}
		
		if (listToTrace.size() > 2){
			this.setListMeanValues();
			g.setColor(Color.ORANGE);
			for (int i=0; i < listMeanValues.size()-1; i++){
				g.drawLine(i*intervalX+leftBorder, (height+topBorder) - (int)(listMeanValues.get(i).getTemperature()*intervalY), (i+1)*intervalX+leftBorder, (height+topBorder) - (int)(listMeanValues.get((i+1)).getTemperature()*intervalY));
			}
			g.drawString(Float.toString(listMeanValues.lastElement().getTemperature()), listMeanValues.size()*intervalX+leftBorder, (height+topBorder) - (int)(listMeanValues.lastElement().getTemperature()*intervalY));
		}
	}

}
