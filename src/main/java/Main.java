import utils.Config;
import webclient.ClientRunner;
import webclient.WebClient;
import webserver.ServerRunner;
import webserver.WebServer;

public class Main {
    public static void main(String args[]) {
        Thread serverThread = new Thread(new ServerRunner());

        Thread clientThread = new Thread(new ClientRunner());

        serverThread.start();
        clientThread.start();
    }
}
