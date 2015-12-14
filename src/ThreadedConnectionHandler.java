/* The Connection Handler Class - Written by Derek Molloy for the EE402 Module
 * See: ee402.eeng.dcu.ie
 */


import java.net.*;
import java.io.*;

public class ThreadedConnectionHandler extends Thread
{
    private Socket clientSocket = null;				// Client socket object
    private ObjectInputStream is = null;			// Input stream
    private ObjectOutputStream os = null;			// Output stream
    private DateTimeService theDateService;
    private TemperatureService theTemperatureService;
    private static int sampleNumber = 0;
    //private Data theData;
    
	// The constructor for the connection handler
    public ThreadedConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        //Set up a service object to get the current date and time
        theDateService = new DateTimeService();
        theTemperatureService = new TemperatureService();
    }

    // Will eventually be the thread execution method - can't pass the exception back
    public void run() {
         try {
            this.is = new ObjectInputStream(clientSocket.getInputStream());
            this.os = new ObjectOutputStream(clientSocket.getOutputStream());
            while (this.readCommand()) {}
         } 
         catch (IOException e) 
         {
        	System.out.println("XX. There was a problem with the Input/Output Communication:");
            e.printStackTrace();
         }
    }

    // Receive and process incoming string commands from client socket 
    private boolean readCommand() {
        Data s = new Data();
        try {
            s = (Data) is.readObject();
        } 
        catch (Exception e){    // catch a general exception
        	this.closeSocket();
            return false;
        }
        System.out.println("01. <- Received a Data object from the client (" + s + ").");
        
        // At this point there is a valid String object
        // invoke the appropriate function based on the command 
        if (s.getDate().equals("GetData")){
        	this.getData();
        }
        else { 
            this.sendError("Invalid command: " + s); 
        }
        return true;
    }
    
    private void getData(){
    	Data theData = new Data();
    	String currentDateTimeText = theDateService.getDateAndTime();
        theData.setDate(currentDateTimeText);
        
        float currentTemperature = theTemperatureService.setTemperature(4);
    	theData.setTemperature(currentTemperature);
    	
    	this.updateSampleNumber();
    	theData.updateSampleNumber(sampleNumber);
    	
    	this.send(theData);
    }
    
    public void updateSampleNumber(){
    	ThreadedConnectionHandler.sampleNumber += 1;
    }

    // Send a generic object back to the client 
    private void send(Object o) {
        try {
            System.out.println("02. -> Sending (" + o +") to the client.");
            this.os.writeObject(o);
            this.os.flush();
        } 
        catch (Exception e) {
            System.out.println("XX." + e.getStackTrace());
        }
    }
    
    // Send a pre-formatted error message to the client 
    public void sendError(String message) { 
        this.send("Error:" + message);	//remember a String IS-A Object!
    }
    
    // Close the client socket 
    public void closeSocket() { //gracefully close the socket connection
        try {
            this.os.close();
            this.is.close();
            this.clientSocket.close();
        } 
        catch (Exception e) {
            System.out.println("XX. " + e.getStackTrace());
        }
    }
}