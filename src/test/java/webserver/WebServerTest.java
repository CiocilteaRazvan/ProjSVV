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
    void testServerEmptiesInputBufferAndPrintsOutput() throws Exception{
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

        webServer.readFromSocket();
        webServer.close();

        verify(mockBufferedReader, times(2)).readLine();
        verify(mockPrintWriter, times(2)).println();
    }

    private Socket getMockSocket() throws IOException {
        return mock(Socket.class);
    }

    private PrintWriter getMockPrintWriter() {
        return mock(PrintWriter.class);
    }

    private BufferedReader getMockBufferedReaderThreeLines() throws IOException {
        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        when(mockBufferedReader.readLine()).thenReturn("Hello World!");
        when(mockBufferedReader.readLine()).thenReturn("quit");
        return mockBufferedReader;
    }
}
