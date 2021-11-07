package utils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMockContainer {
    private ServerSocket mockServerSocket;
    private Socket mockSocket;
    private BufferedReader mockSocketIn;
    private PrintWriter mockSocketOut;
    private PrintWriter mockLogOut;

    public ServerMockContainer(ServerSocket mockServerSocket,
                               Socket mockSocket,
                               BufferedReader mockSocketIn,
                               PrintWriter mockSocketOut,
                               PrintWriter mockLogOut) {
        this.mockServerSocket = mockServerSocket;
        this.mockSocket = mockSocket;
        this.mockSocketIn = mockSocketIn;
        this.mockSocketOut = mockSocketOut;
        this.mockLogOut = mockLogOut;
    }

    public ServerSocket getMockServerSocket() {
        return mockServerSocket;
    }

    public Socket getMockSocket() {
        return mockSocket;
    }

    public BufferedReader getMockSocketIn() {
        return mockSocketIn;
    }

    public PrintWriter getMockSocketOut() {
        return mockSocketOut;
    }

    public PrintWriter getMockLogOut() {
        return mockLogOut;
    }
}
