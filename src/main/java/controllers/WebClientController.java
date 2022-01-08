package controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import utils.Config;
import webclient.WebClient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WebClientController implements Initializable {
    public Button sendButton;
    public TextField commandField;
    public ScrollPane responsePane;
    public VBox responseBox;

    public TextField serverAddress;
    public TextField serverPort;

    WebClient webClient = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverAddress.setText(Config.ADDRESS);
        serverPort.setText(String.valueOf(Config.PORT));

        responseBox = new VBox();
        responsePane.setContent(responseBox);

        responseBox.heightProperty().addListener(observable -> responsePane.setVvalue(1D));
    }

    public void onClickConnectButton(MouseEvent mouseEvent) {
        webClient = new WebClient(serverAddress.getText(), Integer.parseInt(serverPort.getText()));
        webClient.getResponse(responseBox);
    }

    public void onClickSendButton(MouseEvent mouseEvent) {
        try{
            String command = commandField.getText();
            WebClientController.addGuiOutput("Command: " + command, responseBox);

            webClient.sendCommand(commandField.getText());
            commandField.clear();

        } catch (IOException e) {
            System.out.println("Error sending command or receiving response (send button)");
        } catch (NullPointerException e) {
            System.out.println("Client is not connected to server");
        }
    }

    public static void addGuiOutput(String text, VBox responseBox) {
        System.out.println("Adding Label: " + text);
        TextFlow output = new TextFlow(new Text(text));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                responseBox.getChildren().add(output);
            }
        });
    }
}
