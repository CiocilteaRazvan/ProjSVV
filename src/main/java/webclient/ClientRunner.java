package webclient;

import utils.Config;

public class ClientRunner implements Runnable {
    public void run() {
        WebClient webClient = getWebClient();
        webClient.start();
    }

    protected WebClient getWebClient() {
        return new WebClient(Config.ADDRESS, Config.PORT);
    }
}
