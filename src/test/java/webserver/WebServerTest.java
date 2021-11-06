package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.Commands;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class WebServerTest {
    private WebServer webServer;

    @DisplayName("The web server receives two lines then the 'end' command which are printed. After this, the web server closes itself")
    @Test
    void testWebServerReadWriteClose() throws Exception {
        Socket mockSocket = getMockSocket();
        BufferedReader mockSocketIn = getMockSocketIn();
        PrintWriter mockSocketOut = getMockSocketOut();
        webServer = new WebServer(9999) {
            @Override
            protected Socket getSocket (ServerSocket serverSocket) throws IOException {
                return mockSocket;
            }

            @Override
            protected BufferedReader getInStream() throws IOException {
                return mockSocketIn;
            }

            @Override
            protected PrintWriter getOutStream() throws IOException {
                return mockSocketOut;
            }
        };

        webServer.start();

        verify(mockSocketIn, times(3)).readLine();
        verify(mockSocketOut).println("First message");
        verify(mockSocketOut).println("Second message");
        verify(mockSocketOut).println(Commands.END_MESSAGE);
        verify(mockSocketIn, times(1)).close();
        verify(mockSocketOut, times(1)).close();
        verify(mockSocket, times(1)).close();
    }

    private Socket getMockSocket() throws IOException {
        return mock(Socket.class);
    }

    private PrintWriter getMockSocketOut() {
        return mock(PrintWriter.class);
    }

    private BufferedReader getMockSocketIn() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("First message")
                .thenReturn("Second message")
                .thenReturn(Commands.END_MESSAGE);

        return mockBufferedReader;
    }
}
