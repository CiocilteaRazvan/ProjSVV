package webclient;

import utils.Config;

public class ClientRunner implements Runnable{
    public void run() {
        WebClient webClient = getWebClient();
        try {
            webClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected WebClient getWebClient() {
        return new WebClient(Config.ADDRESS, Config.PORT);
    }
}
