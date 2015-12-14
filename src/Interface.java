import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.*;
import java.util.*;

@SuppressWarnings("serial")
public class Interface extends JFrame implements ActionListener, WindowListener {
	private JTextField text_IP, text_Port, text_Delay;
	private JButton pause;
	private String IPAddress, Port, Delay;
	private Vector<Data> theList;
	private Client theApp;
	private Graph graph;
	private int height = 400; //height of the graph
	private int width = 500; //width of the graph

	public Interface() {
		
		
		this.displayDialogBox();
	
		theList = new Vector<Data>();
		graph = new Graph(this.theList, this.height, this.width); //create the graph
		
		this.startClient(); //create and start the Client
		
		// works even if we are using multi-monitor configuration
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();

		JMenuBar menuBar = new JMenuBar(); // create a menubar

		JMenu options = new JMenu("Options"); // create a menu
		JMenuItem changeIP = new JMenuItem("Change the IP address"); // create an item for the menu
		JMenuItem changePort = new JMenuItem("Change the Port number"); // create an item for the menu

		changeIP.addActionListener(this);
		changePort.addActionListener(this);
		
		options.add(changeIP);
		options.add(changePort);

		JMenu file = new JMenu("File"); //create a menu
		JMenuItem exportData = new JMenuItem("Export data"); //create an item for the menu
		
		exportData.addActionListener(this);
		file.add(exportData);
		
		menuBar.add(file); //add the file menu to the menubar
		menuBar.add(options); //add the options to the menubar
		
		JFrame mainFrame = new JFrame(); //create the main window
		mainFrame.setPreferredSize(new Dimension(2*screenWidth/3, 3*screenHeight/4)); //set the size of the mainframe
		mainFrame.setBackground(Color.WHITE); //set the color of the mainFrame
		
		JPanel buttonPanel = new JPanel(); //create the panel for the buttons
		pause = new JButton("Pause"); //create a pause button
		pause.addActionListener(this);
		
		
		buttonPanel.add(pause);
		buttonPanel.setBackground(Color.WHITE); //set the color of the buttonPanel
		mainFrame.add(buttonPanel, BorderLayout.EAST); //add the buttonPanel to the graph
		mainFrame.add(graph, BorderLayout.CENTER); //add the graph to the frame
		mainFrame.setJMenuBar(menuBar); //add the menubar to the Frame
		
		mainFrame.addWindowListener(this);
		mainFrame.pack();
		mainFrame.setLocation(screenWidth * 1/4, screenHeight * 1/4);
		mainFrame.setVisible(true);

	}
	
	// display a dialog box for the IP, Port number and Delay
	public void displayDialogBox(){
		text_IP = new JTextField(20);
		text_IP.setText("192.168.7.2");
		text_Port = new JTextField(20);
		text_Port.setText("5050");
		text_Delay = new JTextField(20);
		text_Delay.setText("1000");
		JComponent[] inputs = new JComponent[] { new JLabel("IP Address"), text_IP, new JLabel("Port Number"),
				text_Port, new JLabel("Delay between each data (in ms)"), text_Delay };

		JOptionPane.showMessageDialog(null, inputs, "Parameters", JOptionPane.PLAIN_MESSAGE);

		this.IPAddress = this.text_IP.getText();
		this.Port = this.text_Port.getText();
		this.Delay = this.text_Delay.getText();
	}
	
	public String displaySaveFileBox(){
		JTextField fileName = new JTextField(20);
		fileName.setText(theList.firstElement().getDate() + "||" + theList.lastElement().getDate());
		JComponent[] inputs = new JComponent[] { new JLabel("Enter your file name"), fileName};
		
		JOptionPane.showMessageDialog(null, inputs, "Export data", JOptionPane.PLAIN_MESSAGE);
		return fileName.getText();
	}
	
	//return the IPAddress written
	public String getIP() {
		return this.IPAddress;
	}

	//return the Port written
	public String getPort() {
		return this.Port;
	}
	
	public void startClient(){
		theList.removeAllElements();
		
		if(this.getIP() != ""){
			theApp = new Client(this.IPAddress, this.theList, this.graph, this.Delay, this.Port);
			theApp.start();
		}
		else
    	{
    		System.out.println("Error: you must provide the address of the server");
    		System.out.println("Usage is:  java Client x.x.x.x  (e.g. java Client 192.168.7.2)");
    		System.out.println("      or:  java Client hostname (e.g. java Client localhost)");
    	}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Export data")){
			this.theApp.changeConnection(); //Pause
			String fileName = this.displaySaveFileBox();
			new FileManager(theList, fileName);
			this.theApp.changeConnection(); //Restart
		}
		if (e.getActionCommand().equals("Change the IP address")){
			this.displayDialogBox();
			this.theApp.changeConnection();
			this.startClient();
		}
		if (e.getActionCommand().equals("Change the Port number")){
			this.displayDialogBox();
			this.theApp.changeConnection();
			this.startClient();
		}
		if (e.getActionCommand().equals("Pause")){
			this.theApp.changeConnection();
			this.pause.setText("Start");
		}
		if (e.getActionCommand().equals("Start")){
			this.theApp.changeConnection();
			this.pause.setText("Pause");
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String args[]) 
    {
    	System.out.println("**. Java Client Application - EE402 OOP Module, DCU");
    	new Interface();
		System.out.println("**. End of Application."); 
    }
	
}
