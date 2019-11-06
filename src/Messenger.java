
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Messenger extends Application {

	public void start(Stage stage) throws Exception {
		stage.setTitle("Messenging App");
		stage.setWidth(400);
		stage.setHeight(400);

		VBox root = new VBox();
		root.setPadding(new Insets(50, 50, 50, 50));
	    root.setSpacing(10);
		
        Scene scene = new Scene(root);
        
        TextField ipText = new TextField("Ip Address");
        ipText.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				ipText.setText("");
			}
        
        });
        TextField portText = new TextField("Port");
        portText.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				portText.setText("");
			}
        
        });
        
        Button createButton = new Button("New Server");
        createButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					Thread thread = new Thread(new HostServer(stage, 
							Integer.parseInt(portText.getText())));
					thread.setDaemon(true);
					thread.start();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
        });
        
        Button connectButton = new Button("New Client");
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent event) {
        		try {
					Thread thread = new Thread(new ClientServer(stage, 
							Integer.parseInt(portText.getText()), ipText.getText()));
					thread.setDaemon(true);
					thread.start();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
        	}
        });
        
        root.getChildren().add(createButton);
        root.getChildren().add(connectButton);
        root.getChildren().add(ipText);
        root.getChildren().add(portText);

        stage.setScene(scene);
        stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
