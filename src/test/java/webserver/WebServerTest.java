package webserver;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WebServerTest {
    WebServer webServer;

    @Test
    void testServerAwaitsConnection() throws Exception{
        Socket mockSocket = getMockSocket();
        BufferedReader mockBufferedReader = getMockBufferedReader();
        webServer = new WebServer(9999) {
            @Override
            protected Socket getSocket (ServerSocket serverSocket) throws IOException {
                return mockSocket;
            }

            @Override
            protected BufferedReader getInStream() throws IOException {
                return mockBufferedReader;
            }
        };

        webServer.readFromSocket();
        webServer.close();
        verify(mockSocket).getOutputStream();
    }

    private Socket getMockSocket() throws IOException {
        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getOutputStream()).thenReturn(mock(OutputStream.class));

        return mockSocket;
    }

    private BufferedReader getMockBufferedReader() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine()).thenReturn("Hello World!");
        when(mockBufferedReader.readLine()).thenReturn("quit");

        return mockBufferedReader;
    }
}
