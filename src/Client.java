/* The Client Class - Written by Derek Molloy for the EE402 Module
 * See: ee402.eeng.dcu.ie
 * 
 * 
 */


import java.net.*;
import java.io.*;
import java.util.*;

public class Client extends Thread{
	
	private int portNumber = 5050;
    private Socket socket = null;
    private ObjectOutputStream os = null;
    private ObjectInputStream is = null;
    private Vector<Data> theList;
    private Graph theGraph;
    private int Delay = 1000;
    private Boolean isConnected = true;

	// the constructor expects the IP address of the server - the port is fixed
    public Client(String serverIP, Vector<Data> aList, Graph graph, String aDelay, String Port) {
    	if (!connectToServer(serverIP)) {
    		System.out.println("XX. Failed to open socket connection to: " + serverIP);            
    	}
    	this.theList = aList;
    	this.theGraph = graph;
    	if (aDelay != null && !aDelay.isEmpty()){ // if the user has input a value for the delay
    		this.Delay = Integer.parseInt(aDelay);
    	}
    	if (Port != null && !Port.isEmpty()){
    		this.portNumber = Integer.parseInt(Port);
    	}
    }

    private boolean connectToServer(String serverIP) {
    	try { // open a new socket to the server 
    		this.socket = new Socket(serverIP,portNumber);
    		this.os = new ObjectOutputStream(this.socket.getOutputStream());
    		this.is = new ObjectInputStream(this.socket.getInputStream());
    		System.out.println("00. -> Connected to Server:" + this.socket.getInetAddress() 
    				+ " on port: " + this.socket.getPort());
    		System.out.println("    -> from local address: " + this.socket.getLocalAddress() 
    				+ " and port: " + this.socket.getLocalPort());
    	} 
        catch (Exception e) {
        	System.out.println("XX. Failed to Connect to the Server at port: " + portNumber);
        	System.out.println("    Exception: " + e.toString());	
        	return false;
        }
		return true;
    }
    
    //method to get the Data
    private void getData(){
    	String theCommand = "GetData";
    	Data theData = new Data();
    	theData.setDate("GetData");
    	
    	System.out.println("01. -> Sending Command (" + theCommand + ") to the server..."); //just to let us know what it is doing
    	this.send(theData); //an object of type Data is sent to the connection handler
    	
    	try{
    		theData = (Data) receive();
    		System.out.println("05. <- The Server responded with: ");
    		theList.add(theData);
    		theGraph.repaint();
    	}
    	catch (Exception e){
    		System.out.println("XX. There was an invalid object sent back from the server");
    		System.out.println("    Exception: " + e.toString());
    	}

    	System.out.println("06. -- Disconnected from Server.");
    }
    
    public Vector<Data> getList(){
    	return theList;
    }
    
    public void changeConnection(){
    	this.isConnected = !this.isConnected;
    	System.out.println("Connection: " + isConnected);
    }
    
    // method to send a generic object.
    private void send(Object o) {
		try {
		    System.out.println("02. -> Sending an object...");
		    os.writeObject(o);
		    os.flush();
		} 
	    catch (Exception e) {
		    System.out.println("XX. Exception Occurred on Sending:" +  e.toString());
		}
    }

    // method to receive a generic object.
    private Object receive() 
    {
		Object o = null;
		try {
			System.out.println("03. -- About to receive an object...");
		    o = is.readObject();
		    System.out.println("04. <- Object received...");
		} 
	    catch (Exception e) {
		    System.out.println("XX. Exception Occurred on Receiving:" + e.toString());
		}
		return o;
    }
    
    
    public void run(){
    	while(true){
    		if(isConnected){
        		this.getData();
        	}
    		try{
        		Thread.sleep(Delay); //sleep for 5sec
        	}
        	catch(InterruptedException e){
        		e.printStackTrace();
        	}
    	}
    }
}