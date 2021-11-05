package webserver;

import utils.Config;

public class ServerRunner implements Runnable{
    public void run() {
        WebServer webServer = new WebServer(Config.PORT);
        try {
            webServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
