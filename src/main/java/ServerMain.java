import applications.ServerApplication;
import webclient.ClientRunner;
import webserver.ServerRunner;

public class ServerMain {
    public static void main (String args[]) {
        Thread serverApplication = new Thread(new ServerApplication());
        serverApplication.start();
    }


}
