package webserver;

import utils.Config;

public class ServerRunner implements Runnable{
    public void run() {
        WebServer webServer = getWebServer();
        webServer.start();
    }

    protected WebServer getWebServer() {
        return new WebServer(Config.PORT);
    }
}
