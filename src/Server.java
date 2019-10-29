import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Server implements Runnable{

	//Data variables
	private int port;
	private String outgoingMessage, incomingMessage, wholeChat;
	private boolean connected;

	//Server Variables
	private Socket socket;
	private ServerSocket serverSocket;
	private DataInputStream input;
	private DataOutputStream output;

	//GUI variables
	private Scene serverScene;
	private Label serverText;
	private TextField serverInput;
	private ScrollPane scroll;
	private Stage stage;
	
	//Constructor
	public Server(Stage stage, int port) {
		this.port = port;
		this.stage = stage;
		
		wholeChat = "";
		
		setupGUI();
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	//Creates sockets and streams
	private void setupServer() {
		try {
			printMessage("Waiting for someone to connect to " + InetAddress.getLocalHost().getHostAddress());
			serverSocket = new ServerSocket(port);
			System.out.println("Waiting");
			socket = serverSocket.accept();
			System.out.println("Connected");
			serverInput.setDisable(false);
			
			
			printMessage(socket.getInetAddress() + " has connected");
			output = new DataOutputStream(socket.getOutputStream());
			connected = true;
			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//Sets up GUIs
	private void setupGUI() {
		VBox root = new VBox();
		serverScene = new Scene(root);
		
		serverInput = new TextField();
		serverInput.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				
				outgoingMessage = serverInput.getText().trim();
				if(outgoingMessage.equals("")) return;
				sendMessage(outgoingMessage);
				
				printMessage("Server" + ": " + outgoingMessage);
				serverInput.setText("");
			}
		});
		serverInput.setPrefHeight(50);
		serverInput.setDisable(true);

		serverText = new Label();
		serverText.setWrapText(true);
		
		scroll = new ScrollPane();
		scroll.setFitToWidth(true);
//		scroll.setPrefWidth(400);
		scroll.setPrefHeight(400 - serverInput.getPrefHeight());
		scroll.setContent(serverText);
		
		
		root.getChildren().add(scroll);
		root.getChildren().add(serverInput);
		
		stage.setScene(serverScene);
	}
	
	//Closes servers
	private void closeServer() {
		try {
			socket.close();
			input.close();
			serverInput.setDisable(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Sends message to client
	private void sendMessage(String string) {
		try {
			output.writeUTF(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Prints message to window
	private void printMessage(String string) {
		wholeChat += string + "\n";
		Platform.runLater(new Runnable(){

			public void run() {
				serverText.setText(wholeChat);
				scroll.setVvalue(scroll.getVmax()+1);
				
			}
		});
		
		if(string.equalsIgnoreCase("Client: quit")) {
			connected = false;
			sendMessage("Chat ending...");
		}
		
	}
	
	public void run() {
		
		setupServer();
		
		while(connected) {
			try {
				incomingMessage = input.readUTF();
				printMessage("Client: " + incomingMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		closeServer();
		
		
	}
	
}
