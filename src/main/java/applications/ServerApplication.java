package applications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerApplication extends Application implements Runnable {
    @Override
    public void run() {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent serverRoot = FXMLLoader.load(getClass().getResource("/fxml/webServer.fxml"));
        Scene serverScene = new Scene(serverRoot);

        primaryStage.setScene(serverScene);
        primaryStage.show();
    }
}
