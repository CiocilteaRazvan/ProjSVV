package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.Config;
import webserver.ServerRunner;
import webserver.WebServer;

import java.net.URL;
import java.util.ResourceBundle;

import static utils.Constants.*;

public class WebServerController implements Initializable {
    public Label webServerStatus;
    public Label webServerAddress;
    public Label webServerPort;

    public Button webServerOnButton;

    public TextField configPort;
    public TextField configRootFolder;

    WebServer webServer = new WebServer();
    Thread serverThread= new Thread(new ServerRunner());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configPort.setText(String.valueOf(Config.PORT));
        configRootFolder.setText(Config.ROOT_FOLDER);
    }

    public void onClickOnButton(MouseEvent mouseEvent) {
        configPort.setEditable(false);
        configRootFolder.setEditable(false);

        webServerAddress.setText(Config.ADDRESS);
        webServerPort.setText(configPort.getText());
        webServerStatus.setText(RUNNING_STATUS);

        int port = Integer.parseInt(configPort.getText());
        webServer.open(port);
        serverThread.start();

    }

    public void onClickOffButton(MouseEvent mouseEvent) {
        try {
            configPort.setEditable(true);
            configRootFolder.setEditable(true);

            webServerAddress.setText(STOPPED_ADDRESS);
            webServerPort.setText(STOPPED_PORT);
            webServerStatus.setText(STOPPED_STATUS);

            webServer.close();

        } catch (NullPointerException e) {
            System.out.println("Server needs to be open before it can close");
        }
    }
}
