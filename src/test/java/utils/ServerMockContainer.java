package utils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerMockContainer {
    private Socket mockSocket;
    private BufferedReader mockUserIn;
    private PrintWriter mockUserOut;
    private BufferedReader mockSocketIn;
    private PrintWriter mockSocketOut;

    public ServerMockContainer(Socket mockSocket,
                               BufferedReader mockUserIn,
                               PrintWriter mockUserOut,
                               BufferedReader mockSocketIn,
                               PrintWriter mockSocketOut) {
        this.mockSocket = mockSocket;
        this.mockUserIn = mockUserIn;
        this.mockUserOut = mockUserOut;
        this.mockSocketIn = mockSocketIn;
        this.mockSocketOut = mockSocketOut;
    }

    public ServerMockContainer(Socket mockSocket,
                               BufferedReader mockSocketIn,
                               PrintWriter mockSocketOut,
                               PrintWriter mockLogOut) {
        this.mockSocket = mockSocket;
        this.mockSocketIn = mockSocketIn;
        this.mockSocketOut = mockSocketOut;
        this.mockUserIn = null;
        this.mockUserOut = mockLogOut;
    }

    public Socket getMockSocket() {
        return mockSocket;
    }

    public BufferedReader getMockUserIn() {
        return mockUserIn;
    }

    public PrintWriter getMockUserOut() {
        return mockUserOut;
    }

    public PrintWriter getMockLogOut() {
        return mockUserOut;
    }

    public BufferedReader getMockSocketIn() {
        return mockSocketIn;
    }

    public PrintWriter getMockSocketOut() {
        return mockSocketOut;
    }
}
