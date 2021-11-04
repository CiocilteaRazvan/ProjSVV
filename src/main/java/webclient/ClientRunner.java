package webclient;

import utils.Config;

public class ClientRunner implements Runnable{
    public void run() {
        WebClient webClient = new WebClient(Config.ADDRESS, Config.PORT);
        try {
            webClient.writeToSocket();
            webClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
