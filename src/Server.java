import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Server{

	//Data variables
	protected int port;
	protected String ipAddress;
	protected String outgoingMessage, incomingMessage, wholeChat;
	protected boolean connected;

	//Server Variables
	protected Socket socket;
//	private ServerSocket serverSocket;
	protected DataInputStream input;
	protected DataOutputStream output;

	//GUI variables
	protected Scene serverScene;
//	private Label serverText;
//	private TextField serverInput;
//	private ScrollPane scroll;
	protected Stage stage;
	protected ChatBox box;
	
	//Constructor
	public Server(Stage stage, int port) {
		this.port = port;
		this.stage = stage;
	}
	
	//Creates sockets and streams
	protected abstract void setupServer(); 
	
	//Sets up GUIs
	protected void setupGUI() {
		wholeChat = "";
		box = new ChatBox(this);
		serverScene = new Scene(box);
		
		stage.setScene(serverScene);
	}
	
	//Closes servers
	protected abstract void closeServer();
	
	//Sends message to client
	public void sendMessage(String string) {
		try {
			output.writeUTF(string);
			printMessage(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Prints message to window
	public void printMessage(String string) {
		wholeChat += string + "\n";
		box.printMessage(string);
		if(string.equalsIgnoreCase("quit")) {
			connected = false;
		}
	}
	
}
