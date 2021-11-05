package utils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMocksContainer {
    private Socket mockSocket = null;
    private BufferedReader mockUserBufferedReader = null;
    private BufferedReader mockSocketBufferedReader = null;
    private PrintWriter mockPrintWriter = null;

    public ClientMocksContainer(Socket mockSocket,
                                BufferedReader mockUserBufferedReader,
                                BufferedReader mockSocketBufferedReader,
                                PrintWriter mockPrintWriter) {
        this.mockSocket = mockSocket;
        this.mockUserBufferedReader = mockUserBufferedReader;
        this.mockSocketBufferedReader = mockSocketBufferedReader;
        this.mockPrintWriter = mockPrintWriter;
    }

    public Socket getMockSocket() {
        return mockSocket;
    }

    public BufferedReader getMockUserBufferedReader() {
        return mockUserBufferedReader;
    }

    public BufferedReader getMockSocketBufferedReader() {
        return mockSocketBufferedReader;
    }

    public PrintWriter getMockPrintWriter() {
        return mockPrintWriter;
    }
}
