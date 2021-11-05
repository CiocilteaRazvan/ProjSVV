package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class WebServerTest {
    private WebServer webServer;

    @DisplayName("The web server receives two lines then the 'end' command which are printed. After this, the web server closes itself")
    @Test
    void testWebServerReadWriteClose() throws Exception{
        BufferedReader mockBufferedReader = getMockBufferedReaderThreeLines();
        PrintWriter mockPrintWriter = getMockPrintWriter();
        Socket mockSocket = getMockSocket();
        webServer = new WebServer(9999) {
            @Override
            protected Socket getSocket (ServerSocket serverSocket) throws IOException {
                return mockSocket;
            }

            @Override
            protected BufferedReader getInStream() throws IOException {
                return mockBufferedReader;
            }

            @Override
            protected PrintWriter getOutStream() throws IOException {
                return mockPrintWriter;
            }
        };

        webServer.start();

        verify(mockBufferedReader, times(3)).readLine();
        verify(mockPrintWriter).println("First message");
        verify(mockPrintWriter).println("Second message");
        verify(mockPrintWriter).println("end");
        verify(mockBufferedReader, times(1)).close();
        verify(mockPrintWriter, times(1)).close();
        verify(mockSocket, times(1)).close();
    }

    private Socket getMockSocket() throws IOException {
        return mock(Socket.class);
    }

    private PrintWriter getMockPrintWriter() {
        return mock(PrintWriter.class);
    }

    private BufferedReader getMockBufferedReaderThreeLines() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine())
                .thenReturn("First message")
                .thenReturn("Second message")
                .thenReturn("end");

        return mockBufferedReader;
    }
}
