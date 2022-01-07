package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import utils.Config;
import webclient.ClientRunner;
import webclient.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class WebClientController implements Initializable {
    public Button sendButton;
    public TextField commandField;
    public ScrollPane responsePane;

    public TextField serverAddress;
    public TextField serverPort;

    WebClient webClient = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverAddress.setText(Config.ADDRESS);
        serverPort.setText(String.valueOf(Config.PORT));
    }

    public void onClickConnectButton(MouseEvent mouseEvent) {
        webClient = new WebClient(serverAddress.getText(), Integer.parseInt(serverPort.getText()));
        webClient.getResponse(responsePane);
    }

    public void onClickSendButton(MouseEvent mouseEvent) {
        try{
            String command = commandField.getText();
            WebClientController.addLabel("Command: " + command, responsePane);

            webClient.sendCommand(commandField.getText());
            commandField.clear();

        } catch (IOException e) {
            System.out.println("Error sending command or receiving response (send button)");
        } catch (NullPointerException e) {
            System.out.println("Client is not connected to server");
        }
    }

    public static void addLabel(String text, ScrollPane responsePane) {
        System.out.println("Adding Label: " + text);
    }
}
