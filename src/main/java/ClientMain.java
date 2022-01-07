import applications.ClientApplication;

public class ClientMain {
    public static void main (String args[]) {
        Thread clientApplication = new Thread(new ClientApplication());
        clientApplication.start();
    }
}
