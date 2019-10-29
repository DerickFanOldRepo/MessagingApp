import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

public class Client implements Runnable{

	// Data variables
	private int port;
	private String ipAddress;
	private String outgoingMessage, incomingMessage, wholeChat;
	private boolean connected;
	private String name;

	// Server variables
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;

	// GUI variables
	private Label clientText;
	private Scene clientScene;
	private TextField clientInput;
	private ScrollPane scroll;
	private Stage stage;

	// Constructor
	public Client(Stage stage, String ipAddress) {
		this.ipAddress = ipAddress;
		this.stage = stage;
		initialStartUp();
		
		setupGUI();
		
	}

	private void initialStartUp() {
		connected = false;
		name = "Client";
		wholeChat = "";
	}
	
	private void tryConnection() {	
		System.out.println(ipAddress);
		try {
			socket = new Socket(ipAddress, 12345);
			connected = true;
			setupServer();	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setupServer() throws IOException{
		connected = true;
		printMessage("Connected");
		output = new DataOutputStream(socket.getOutputStream());
		input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));	
	}
	

	// Sets up GUIs
	private void setupGUI() {
		VBox root = new VBox();
		clientScene = new Scene(root);
		
		clientInput = new TextField();
		clientInput.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				
				outgoingMessage = clientInput.getText().trim();
				if(outgoingMessage.equals("")) return;
				sendMessage(outgoingMessage);
				
				printMessage(name + ": " + outgoingMessage);
				clientInput.setText("");
			}
		});
		clientInput.setPrefHeight(50);
		clientText = new Label();
		clientText.setWrapText(true);
		
		scroll = new ScrollPane();
		scroll.setFitToWidth(true);
		scroll.setVvalue(1.0);
		scroll.setPrefHeight(400 - clientInput.getPrefHeight());
		scroll.setContent(clientText);
		
		root.getChildren().add(scroll);
		root.getChildren().add(clientInput);
		
		stage.setScene(clientScene);
	}
	
	// Closes servers
	private void closeServer() {
		try {
			socket.close();
			input.close();
			clientInput.setDisable(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(String string) {
		try {
			output.writeUTF(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printMessage(String string) {
		wholeChat += string + "\n";
		Platform.runLater(new Runnable(){
			public void run() {
				clientText.setText(wholeChat);
				scroll.setVvalue(1.0);
			}
		});
		if (string.equalsIgnoreCase(name + ": quit")) connected = false;
	}
	
	public void run() {
		
		tryConnection();
		
		while(connected) {
			try {
				incomingMessage = input.readUTF();
				printMessage("Server: " + incomingMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		closeServer();
		
	}

}
