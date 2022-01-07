package applications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApplication extends Application implements Runnable{
    @Override
    public void run() {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent clientRoot = FXMLLoader.load(getClass().getResource("/fxml/webClient.fxml"));
        Scene clientScene = new Scene(clientRoot);

        primaryStage.setScene(clientScene);
        primaryStage.show();
    }
}
