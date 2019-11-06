import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ChatBox extends VBox{
	
	private Label text;
	private TextField input;
	private ScrollPane scroll;
	
	private Server server;

	public ChatBox(Server s) {
		super();
		setupGUI();
		server = s;
	}
	
	public void setupGUI() {
		input = new TextField();
		input.setPrefHeight(50);
		input.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				sendMessage(input.getText());
			}
			
		});
		text = new Label();
		text.setWrapText(true);
		
		scroll = new ScrollPane();
		scroll.setFitToWidth(true);
		scroll.setVvalue(1.0);
		scroll.setPrefHeight(400 - input.getPrefHeight());
		scroll.setContent(text);
		
		this.getChildren().add(scroll);
		this.getChildren().add(input);
		
	}
	
	public void printMessage(String msg) {
		Platform.runLater(new Runnable() {
			public void run() {
				text.setText(text.getText() + msg + "\n");
			}
		});
	}

	public void sendMessage(String msg) {
		server.sendMessage(msg);
//		printMessage(msg);
		input.setText("");
	}

}