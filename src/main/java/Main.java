import webclient.ClientRunner;
import webserver.ServerRunner;

public class Main {
    public static void main (String args[]) {
        Thread serverThread = new Thread(new ServerRunner());
        Thread clientThread = new Thread(new ClientRunner());

        serverThread.start();
        clientThread.start();
    }
}
