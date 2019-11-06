import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.stage.Stage;

public class ClientServer extends Server implements Runnable{

	// Constructor
	public ClientServer(Stage stage, int port, String ipAddress) {
		super(stage, port);
		this.ipAddress = ipAddress;
		this.port = port;
		
		setupGUI();
	}

	private void initialStartUp() {
		connected = false;
//		name = "Client";
		wholeChat = "";
	}
	
	private void tryConnection() {	
		try {
			System.out.println(ipAddress);	
			System.out.println(port);
			socket = new Socket(ipAddress, port);
			connected = true;
			setupServer();	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
//			System.out.println("ERROR");
			e.printStackTrace();
		}
	}

	protected void setupServer(){
		printMessage("Connected");
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Closes servers
	protected void closeServer() {
		try {
			socket.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {				
		initialStartUp();
		tryConnection();
		
		while(connected) {
			try {
				incomingMessage = input.readUTF();
				printMessage(incomingMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
		closeServer();
		
	}

}
