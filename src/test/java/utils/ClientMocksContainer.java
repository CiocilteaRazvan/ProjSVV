package utils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMocksContainer {
    private Socket mockSocket = null;
    private BufferedReader mockUserIn = null;
    private BufferedReader mockSocketIn = null;
    private PrintWriter mockSocketOut = null;

    public ClientMocksContainer(Socket mockSocket,
                                BufferedReader mockUserIn,
                                BufferedReader mockSocketIn,
                                PrintWriter mockSocketOut) {
        this.mockSocket = mockSocket;
        this.mockUserIn = mockUserIn;
        this.mockSocketIn = mockSocketIn;
        this.mockSocketOut = mockSocketOut;
    }

    public Socket getMockSocket() {
        return mockSocket;
    }

    public BufferedReader getMockUserIn() {
        return mockUserIn;
    }

    public BufferedReader getMockSocketIn() {
        return mockSocketIn;
    }

    public PrintWriter getMockSocketOut() {
        return mockSocketOut;
    }
}
