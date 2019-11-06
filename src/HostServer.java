import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HostServer extends Server implements Runnable{

	//Data variables

	//Server Variables
	private ServerSocket serverSocket;
	
	//Constructor
	public HostServer(Stage stage, int port) {
		super(stage, port);
		
		setupGUI();	
	}
	
	//Creates sockets and streams
	protected void setupServer() {
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
			printMessage("Waiting for someone to connect to " + ipAddress);
			serverSocket = new ServerSocket(port);
			System.out.println("Waiting");
			socket = serverSocket.accept();
			connected = true;
			System.out.println("Connected");

			printMessage(socket.getInetAddress() + " has connected");
			output = new DataOutputStream(socket.getOutputStream());
			connected = true;
			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//Closes servers
	protected void closeServer() {
		try {
			socket.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		setupServer();
		
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
