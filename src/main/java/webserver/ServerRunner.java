package webserver;

import utils.Config;

public class ServerRunner implements Runnable{
    public void run() {
        WebServer webServer = getWebServer();
        try {
            webServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected WebServer getWebServer() {
        return new WebServer(Config.PORT);
    }

}
