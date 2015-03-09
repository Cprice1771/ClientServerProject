/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCPClient;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Cprice
 */
public class TCPClientForm extends Application {
    
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("TCPClient");
        
        final TCPClient client = new TCPClient();
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(grid, 330, 175);
        primaryStage.setScene(scene);
        
        Text scenetitle = new Text("TCPClient info");
        scenetitle.setFont(Font.font("Connect", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label serverAddrLabel = new Label("Router IP/Port:");
       
        grid.add(serverAddrLabel, 0, 1);

        final TextField routerAddrTextField = new TextField();
        routerAddrTextField.setText("172.16.20.21");
        grid.add(routerAddrTextField, 1, 1);
        
        final TextField portBox = new TextField();
        portBox.setPrefWidth(50);
        portBox.setText("5555");
        grid.add(portBox, 3, 1);

        Label portLabel = new Label("Server IP:");
        grid.add(portLabel, 0, 2);
        
        final TextField serverAddrBox = new TextField();
        serverAddrBox.setText("172.16.20.21");
        grid.add(serverAddrBox, 1, 2);
        
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Send");
        
        Button openButton = new Button("Select a File");
        grid.add(openButton, 0, 3);
        
        final TextField fileTextField = new TextField();
        fileTextField.setText("C:\\Users\\Cprice\\Desktop\\DistComputingData\\avatar.jpg");
        grid.add(fileTextField, 1, 3);
        
        
        
        Button sendButton = new Button("Send File");
        grid.add(sendButton, 0, 4);
        
        final TextField sendTimes = new TextField();
        sendTimes.setText("2");
        grid.add(sendTimes, 1, 4);
        
        openButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        fileTextField.setText(file.getAbsolutePath());
                    }
                }
            });
        
         sendButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    client.setServerName(serverAddrBox.getText());
                    client.setRouterName(routerAddrTextField.getText());
                    client.SetRouterPort(Integer.parseInt(portBox.getText()));
                    
                    System.out.println("Server name: " + serverAddrBox.getText());
                    System.out.println("Router name: " + routerAddrTextField.getText());
                    System.out.println("Router port: " + portBox.getText());
                    for(int i = 0; i < Integer.parseInt(sendTimes.getText()); i++)
                    {
                        try {
                            String file = fileTextField.getText();
                            String[] fileParts = file.split("\\.");
                            String ext = fileParts[fileParts.length - 1];
                            
                            if(ext.equals("jpg") || ext.equals("jpeg") ||ext.equals("png"))
                                client.SendImage(fileTextField.getText());
                            else
                                client.SendFile(fileTextField.getText());
                        } catch (IOException ex) {
                            Logger.getLogger(TCPClientForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
                }
            });

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
