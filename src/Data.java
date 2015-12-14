import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public class Data implements Serializable{
	private float Temperature;
	private String Date;
	private int sampleNumber=0;
	
	
	public void setDate(String currentDateTimeText){
		this.Date = currentDateTimeText;
	}
	
	public void setTemperature(float currentTemperature){
		this.Temperature = currentTemperature;
	}
	
	public void updateSampleNumber(int theSampleNumber){
		this.sampleNumber = theSampleNumber;
	}
	
	public void display(){
		System.out.println("    <- " + Temperature);
		System.out.println("    <- " + Date);
		System.out.println("    <- " + sampleNumber);
	}
	
	public float getTemperature(){
		return this.Temperature;
	}
	
	public String getDate(){
		return this.Date;
	}
	
	public int getSampleNumber(){
		return this.sampleNumber;
	}
	
	public ArrayList<Data> add(ArrayList<Data> theList){
		int i=0;
		for (i=0; i<theList.size(); i++)
			if (theList.get(i) != null){ //search for a null element
				i++;
			}
			if (i == theList.size()){ //if the array is full
				for (int j=0; j<theList.size()-1; j++){
					theList.set(j, theList.get(j+1));
				}
				theList.add(theList.size()-1, this); //add the element at the end of the array
			}
			else { //if their is a null element
				theList.add(i, this); //add the element at the first null element
			}
		System.out.println("    theList contains: " + theList);
		return theList;
	}
}
