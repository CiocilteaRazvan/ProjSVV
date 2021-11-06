import webclient.ClientRunner;
import webserver.ServerRunner;

public class Main {
    public static void main (String args[]) {
        Thread serverThread = new Thread(new ServerRunner());
        Thread clientThread = new Thread(new ClientRunner());

        //TODO FIXIT server does not print to System.out messages from client
        serverThread.start();
        clientThread.start();
    }
}
